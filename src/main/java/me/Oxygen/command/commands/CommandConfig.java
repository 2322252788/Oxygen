package me.Oxygen.command.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import me.Oxygen.Oxygen;
import me.Oxygen.command.Command;
import me.Oxygen.ui.ClientNotification.Type;
import me.Oxygen.utils.ClientUtil;
import net.minecraft.client.Minecraft;

public class CommandConfig extends Command {
    public CommandConfig(String[] commands) {
        super(commands);
        this.setArgs("-Debug");
    }

    @Override
    public void onCmd(String[] args) {
    	 if (args.length == 2) {
             if (!get("http://gitee.com/rainbow233/Oxygen/raw/master/Config/" + args[0].toLowerCase() + "/Values.txt").equals("")) {
                 writeValue(get("http://gitee.com/rainbow233/Oxygen/raw/master/Config/" + args[0].toLowerCase() + "/Values.txt"));
                 writeMods(get("http://gitee.com/rainbow233/Oxygen/raw/master/Config/" + args[0].toLowerCase() + "/Mods.txt"));

					try {
						Oxygen.INSTANCE.FileMgr.loadMods();
					} catch (IOException e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					}

                 try {
					Oxygen.INSTANCE.FileMgr.loadValues();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
             }
             else {
                 ClientUtil.sendClientMessage("We do not have this config!", Type.INFO);
             }
         }
         else {
        	 ClientUtil.sendClientMessage(".config [server]", Type.INFO);
         }

    }
    
    public static void writeValue(final String s) {
        final File file = new File(Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + "/" + Oxygen.INSTANCE.CLIENT_NAME + "/values.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            final PrintWriter printWriter = new PrintWriter(file);
            printWriter.print(s);
            printWriter.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void writeMods(final String s) {
        final File file = new File(Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + "/" + Oxygen.INSTANCE.CLIENT_NAME + "/mods.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            final PrintWriter printWriter = new PrintWriter(file);
            printWriter.print(s);
            printWriter.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static String get(String url) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url;
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.setDoOutput(true);
            connection.setReadTimeout(99781);
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppIeWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36");
            connection.connect();
            Map<String, List<String>> map = connection.getHeaderFields();
            for (String key : map.keySet()) { }
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line + "\n";
            }
        } catch (Exception e) {
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
            }
        }
  	return result;
    }
}
