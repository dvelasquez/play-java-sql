package security;

/**
 * Created by admin on 23-06-2015.
 */
import play.Configuration;
import play.libs.F;
import play.libs.Json;
import play.mvc.*;
import play.mvc.Http.Context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;

public class RestSecurity {

    @With(RestAuthenticatedAction.class)
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface JWTAuth {
        Class<? extends RestAuthenticator> value() default RestAuthenticator.class;
    }
    public static final String securityHash = Configuration.root().getString("play.crypto.secret");

    /**
     * Wraps another action, allowing only authenticated HTTP requests.
     * <p>
     * The user name is retrieved from the session cookie, and added to the HTTP request's
     * <code>username</code> attribute.
     */
    public static class RestAuthenticatedAction extends Action<JWTAuth> {

        public F.Promise<Result> call(final Context ctx) {
            try {
                RestAuthenticator authenticator = configuration.value().newInstance();
//                String username = authenticator.getUsername(ctx);
                String jsonWebToken = authenticator.getJWT(ctx);
                if(jsonWebToken == null) {
                    Result unauthorized = authenticator.onUnauthorized(ctx);
                    return F.Promise.pure(unauthorized);
                } else {
                    try {
                        ctx.response().setHeader("Authorization", jsonWebToken);
                        return delegate.call(ctx).transform(
                                new F.Function<Result, Result>() {
                                    @Override
                                    public Result apply(Result result) throws Throwable {
                                        return result;
                                    }
                                },
                                throwable -> {
                                    ctx.response().setHeader("Authorization", jsonWebToken);
                                    return throwable;
                                }
                        );
                    } catch(Exception e) {
                        ctx.response().setHeader("Authorization", jsonWebToken);
                        throw e;
                    }
                }
            } catch(RuntimeException e) {
                throw e;
            } catch(Throwable t) {
                throw new RuntimeException(t);
            }
        }

    }

    /**
     * Handles authentication.
     */
    public static class RestAuthenticator extends Results {

        /**
         * Retrieves the username from the HTTP context; the default is to read from the session cookie.
         *
         * @return null if the user is not authenticated.
         */
        public String getJWT(Context ctx) {
//            return ctx.session().get("username");
            return ctx.request().getHeader("Authorization");
        }

        /**
         * Generates an alternative result if the user is not authenticated; the default a simple '401 Not Authorized' page.
         */
        public Result onUnauthorized(Context ctx) {
            HashMap<String, String> error = new HashMap<>();
            error.put("error", "unauthorized");
            return unauthorized(Json.toJson(error));
        }

    }
}
