package me.Oxygen.utils.other;

import me.Oxygen.Oxygen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class ChatUtils {
	private final ChatComponentText message;

	private ChatUtils(ChatComponentText message) {
		this.message = message;
	}

	public static String addFormat(String message, String regex) {
		return message.replaceAll("(?i)" + regex + "([0-9a-fklmnor])", "\u00a7$1");
	}

	public void displayClientSided() {
		Minecraft.getMinecraft().thePlayer.addChatMessage(this.message);
	}

	private ChatComponentText getChatComponent() {
		return this.message;
	}

	/* synthetic */ ChatUtils(ChatComponentText chatComponentText, ChatUtils chatUtils) {
		this(chatComponentText);
	}

	public static class ChatMessageBuilder {
		private static final EnumChatFormatting defaultMessageColor = EnumChatFormatting.WHITE;
		private ChatComponentText theMessage = new ChatComponentText("");
		private boolean useDefaultMessageColor = false;
		private ChatStyle workingStyle = new ChatStyle();
		private ChatComponentText workerMessage = new ChatComponentText("");

		public ChatMessageBuilder(boolean prependDefaultPrefix, boolean useDefaultMessageColor) {
			if (prependDefaultPrefix) {
				Oxygen.INSTANCE.getClass();
				this.theMessage.appendSibling(new ChatMessageBuilder(false, false)
						.appendText(String
								.valueOf((Object) ((Object) EnumChatFormatting.GRAY) + "["
										+ (Object) ((Object) EnumChatFormatting.AQUA))
								+ Oxygen.INSTANCE.CLIENT_NAME + (Object) ((Object) EnumChatFormatting.GRAY) + "] ")
						.setColor(EnumChatFormatting.RED).build().getChatComponent());
			}
			this.useDefaultMessageColor = useDefaultMessageColor;
		}

		public ChatMessageBuilder() {
		}

		public ChatMessageBuilder appendText(String text) {
			this.appendSibling();
			this.workerMessage = new ChatComponentText(text);
			this.workingStyle = new ChatStyle();
			if (this.useDefaultMessageColor) {
				this.setColor(defaultMessageColor);
			}
			return this;
		}

		public ChatMessageBuilder setColor(EnumChatFormatting color) {
			this.workingStyle.setColor(color);
			return this;
		}

		public ChatMessageBuilder bold() {
			this.workingStyle.setBold(true);
			return this;
		}

		public ChatMessageBuilder italic() {
			this.workingStyle.setItalic(true);
			return this;
		}

		public ChatMessageBuilder strikethrough() {
			this.workingStyle.setStrikethrough(true);
			return this;
		}

		public ChatMessageBuilder underline() {
			this.workingStyle.setUnderlined(true);
			return this;
		}

		public ChatUtils build() {
			this.appendSibling();
			return new ChatUtils(this.theMessage, null);
		}

		private void appendSibling() {
			this.theMessage.appendSibling(this.workerMessage.setChatStyle(this.workingStyle));
		}
	}

	public static void printChat(String text) {
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(text));
    }

	

}
