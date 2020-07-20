package me.Oxygen.injection.mixins;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.google.common.collect.Ordering;

import me.Oxygen.injection.interfaces.IGuiPlayerTabOverlay;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;

@Mixin(GuiPlayerTabOverlay.class)
public class MixinGuiPlayerTabOverlay extends Gui implements IGuiPlayerTabOverlay {
	
	@Final
	@Shadow
	private static Ordering<NetworkPlayerInfo> field_175252_a;

	@Override
	public Ordering<NetworkPlayerInfo> getField_175252_a() {
		return MixinGuiPlayerTabOverlay.field_175252_a;
	}
	
	/*@Overwrite
    public String getPlayerName(final NetworkPlayerInfo networkPlayerInfoIn) {
        if (PlayersCheck.isPlayer(networkPlayerInfoIn.getGameProfile().getName())) {
            return (networkPlayerInfoIn.getDisplayName() != null) ? ("§a[Invincible]§r" + networkPlayerInfoIn.getDisplayName().getFormattedText()) : ScorePlayerTeam.formatPlayerName((Team)networkPlayerInfoIn.getPlayerTeam(), "§a[Invincible]§r" + networkPlayerInfoIn.getGameProfile().getName());
        }
        return (networkPlayerInfoIn.getDisplayName() != null) ? networkPlayerInfoIn.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName((Team)networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
    }*/

	
}
