package me.Oxygen.ui.altManager;

import java.net.Proxy;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

import me.Oxygen.injection.interfaces.IMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public final class AltLoginThread extends Thread {
    private final String password;
    private final String username;
    private Minecraft mc = Minecraft.getMinecraft();
    public AltLoginThread(String username, String password) {
        super("Alt Login Thread");
        this.username = username;
        this.password = password;
    }

    private Session createSession(String username, String password) {
        YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
        auth.setUsername(username);
        auth.setPassword(password);
        try {
            auth.logIn();
            return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
        }
        catch (AuthenticationException localAuthenticationException) {
            localAuthenticationException.printStackTrace();
            return null;
        }
    }

    @Override
    public void run() {
        if (this.password.equals("OfflineAccountName")) {
            ((IMinecraft) this.mc).setSession(new Session(this.username, "", "", "mojang"));
            GuiAltManager.status = "Logged in. (" + this.username + " - offline name)";
            return;
        }
        GuiAltManager.status = "Logging in...";
        Session auth = this.createSession(this.username, this.password);
        if (auth == null) {
        	GuiAltManager.status = "Login failed!";
        }else{
          
        	GuiAltManager.status = "Logged in. (" + auth.getUsername() + ")";
            ((IMinecraft) this.mc).setSession(auth);
        }
    }
}

