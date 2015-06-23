package security;

import com.valentys.sdk.adapter.Adapter;
import play.mvc.Http;
import play.mvc.With;
import security.models.UserDTO;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import java.util.Map;

/**
 * Created by Danilo Velasquez on 23-06-2015.
 */
@With(RestSecurity.RestAuthenticatedAction.class)
public class RestController extends Adapter {
    public static UserDTO getProfile(Http.Request request) throws Exception {
        UserDTO user = new UserDTO();
//        SSOFactory factory = new SSOFactory();
        String jwt = request.getHeader("Authorization");
        final JWTVerifier jwtVerifier = new JWTVerifier(RestSecurity.securityHash, "*");

        Map<String, Object> data = jwtVerifier.verify(jwt);
//        user. = data.get("USU_Identificador").toString();
//        user = factory.getLogin(user.identificador);
        user.setUSU_Nombre("Hola");
        return user;
    }
}
