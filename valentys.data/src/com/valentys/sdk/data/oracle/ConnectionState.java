package com.valentys.sdk.data.oracle;

/**
 * Created by davidmunozgaete on 22-08-14.
 */
public class ConnectionState {

    private boolean active;

    public ConnectionState() {
        this.active = true;
    }

    public ConnectionState(boolean isAvailable) {
        this.active = isAvailable;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean available) {
        this.active = available;
    }

}
