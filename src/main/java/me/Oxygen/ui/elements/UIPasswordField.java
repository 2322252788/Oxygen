package me.Oxygen.ui.elements;

import net.minecraft.client.gui.FontRenderer;

public class UIPasswordField extends UITextField {
	
	public UIPasswordField(int componentId, FontRenderer fontrendererObj, int x, int y, int par5Width, int par6Height) {
		super(componentId, fontrendererObj, x, y, par5Width, par6Height);
	}
	
	@Override
	public void drawTextBox() {
		String realText = this.getText();
		StringBuilder stringBuilder = new StringBuilder();
		int var3 = 0;

		for (int var4 = ((CharSequence) this.getText()).length(); var3 < var4; ++var3) {
			stringBuilder.append('*');
		}

		this.setText(stringBuilder.toString());
		super.drawTextBox();
		this.setText(realText);
	}
}
