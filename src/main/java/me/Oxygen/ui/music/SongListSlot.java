package me.Oxygen.ui.music;

import me.Oxygen.Oxygen;
import me.Oxygen.manager.MusicManager;
import me.Oxygen.utils.music.SongList;
import me.Oxygen.utils.render.Colors;
import me.Oxygen.utils.render.RenderUtil;

public class SongListSlot {
	public SongList list;
	
	public SongListSlot(SongList a) {
			this.list = a;
	}
	
	public void draw(int mouseX, int mouseY, float x, float y) {
		
		int col = MusicManager.instance.currentSongList == list ? Colors.BLACK.c : Colors.GREY.c;
		
		if(MusicManager.instance.currentSongList == list) {
			RenderUtil.drawRect(x - 4, y - 6, x + 2, y + 16, Colors.AQUA.c);
			RenderUtil.drawRect(x + 2, y - 6, x + 120, y + 16, RenderUtil.reAlpha(Colors.GREY.c, 0.4f));
		}
		
		Oxygen.INSTANCE.font.wqy16.drawString(list.name, x + 24f, y, col);
		RenderUtil.drawImage(list.res, (int) x + 4, (int) y - 3, 16, 16, 1.0f);
	}
	
	public void onCrink() {
		if(MusicManager.instance.currentSongList != list) {
			MusicManager.instance.currentSongList = list;
		}
	}
}
