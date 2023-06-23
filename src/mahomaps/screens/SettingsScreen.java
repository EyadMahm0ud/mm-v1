package mahomaps.screens;

import java.io.IOException;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;

import mahomaps.MahoMapsApp;
import mahomaps.Settings;
import tube42.lib.imagelib.ImageUtils;

public class SettingsScreen extends Form implements CommandListener {

	private ChoiceGroup focusZoom = new ChoiceGroup(MahoMapsApp.text[46], Choice.POPUP,
			new String[] { "15", "16", "17", "18" }, null);
	private ChoiceGroup geoLook;
	private ChoiceGroup geoStatus = new ChoiceGroup(MahoMapsApp.text[47], Choice.POPUP,
			new String[] { MahoMapsApp.text[48], MahoMapsApp.text[49], MahoMapsApp.text[50] }, null);
	private ChoiceGroup tileInfo = new ChoiceGroup(MahoMapsApp.text[51], Choice.POPUP,
			new String[] { MahoMapsApp.text[18], MahoMapsApp.text[17] }, null);
	private ChoiceGroup cache = new ChoiceGroup(MahoMapsApp.text[52], Choice.POPUP,
			new String[] { MahoMapsApp.text[18], MahoMapsApp.text[53], "RMS" }, null);
	private ChoiceGroup download = new ChoiceGroup(MahoMapsApp.text[54], Choice.POPUP,
			new String[] { MahoMapsApp.text[18], MahoMapsApp.text[55], }, null);
	private ChoiceGroup proxyTiles = new ChoiceGroup(MahoMapsApp.text[19], Choice.POPUP,
			new String[] { MahoMapsApp.text[18], "nnchan.ru", }, null);
	private ChoiceGroup uiSize = new ChoiceGroup(MahoMapsApp.text[56], Choice.POPUP,
			new String[] { MahoMapsApp.text[57], "50x50", "30x30" }, null);
	private ChoiceGroup lang = new ChoiceGroup(MahoMapsApp.text[58], Choice.POPUP,
			new String[] { MahoMapsApp.text[59], MahoMapsApp.text[60], MahoMapsApp.text[61] }, null);
	private ChoiceGroup uiLang = new ChoiceGroup(MahoMapsApp.text[62], Choice.POPUP,
			new String[] { "Русский", "English", "Français" }, null);
	private ChoiceGroup bbNetwork = new ChoiceGroup(MahoMapsApp.text[63], Choice.EXCLUSIVE,
			new String[] { MahoMapsApp.text[64], "Wi-Fi" }, null);
	private ChoiceGroup map = new ChoiceGroup("Map", Choice.EXCLUSIVE,
			new String[] { "Scheme", "Satellite" }, null);

	public SettingsScreen() {
		super(MahoMapsApp.text[11]);
		addCommand(MahoMapsApp.back);
		setCommandListener(this);
		Image[] imgs = null;
		try {
			final int size = 20;
			Image sheet = ImageUtils.halve(Image.createImage("/geo40.png"));

			Image i1 = Image.createImage(size, size);
			i1.getGraphics().drawImage(sheet, 0, 0, 0);
			Image i2 = Image.createImage(size, size);
			i2.getGraphics().drawImage(sheet, 0, -size, 0);
			Image i3 = Image.createImage(size, size);
			i3.getGraphics().drawImage(sheet, 0, -(size * 2), 0);
			Image i4 = Image.createImage(size, size);
			i4.getGraphics().drawImage(sheet, 0, -(size * 3), 0);
			imgs = new Image[] { i1, i2, i3, i4 };
		} catch (IOException e) {
		}
		geoLook = new ChoiceGroup(MahoMapsApp.text[65], Choice.EXCLUSIVE,
				new String[] { MahoMapsApp.text[66], "\"Я\"", "\"Ы\"", "\"Ъ\"" }, imgs);
		if (Settings.focusZoom < 15)
			Settings.focusZoom = 15;
		if (Settings.focusZoom > 18)
			Settings.focusZoom = 18;
		focusZoom.setSelectedIndex(Settings.focusZoom - 15, true);
		geoLook.setSelectedIndex(Settings.geoLook, true);
		geoStatus.setSelectedIndex(Settings.showGeo, true);
		tileInfo.setSelectedIndex(Settings.drawDebugInfo ? 1 : 0, true);
		cache.setSelectedIndex(Settings.cacheMode, true);
		download.setSelectedIndex(Settings.allowDownload ? 1 : 0, true);
		proxyTiles.setSelectedIndex(Settings.proxyTiles ? 1 : 0, true);
		// апи отслеживается отдельно, однако предполагается что оно включено вместе с
		// тайлами.
		uiSize.setSelectedIndex(Settings.uiSize, true);
		lang.setSelectedIndex(Settings.apiLang, true);
		uiLang.setSelectedIndex(Settings.uiLang, true);
		map.setSelectedIndex(Settings.map, true);

		append(focusZoom);
		append(geoLook);
		append(geoStatus);
		append(tileInfo);
		append(cache);
		append(download);
		append(proxyTiles);
		append(uiSize);
		append(lang);
		append(uiLang);
		append(map);
		if (MahoMapsApp.bb) {
			bbNetwork.setSelectedIndex(Settings.bbWifi ? 1 : 0, true);
			append(bbNetwork);
		}
	}

	private void Apply() {
		Settings.focusZoom = focusZoom.getSelectedIndex() + 15;
		Settings.geoLook = geoLook.getSelectedIndex();
		Settings.showGeo = geoStatus.getSelectedIndex();
		Settings.drawDebugInfo = tileInfo.getSelectedIndex() == 1;
		Settings.cacheMode = cache.getSelectedIndex();
		Settings.allowDownload = download.getSelectedIndex() == 1;
		Settings.proxyTiles = proxyTiles.getSelectedIndex() == 1;
		Settings.proxyApi = proxyTiles.getSelectedIndex() == 1;
		Settings.uiSize = uiSize.getSelectedIndex();
		Settings.apiLang = lang.getSelectedIndex();
		Settings.uiLang = uiLang.getSelectedIndex();
		if (MahoMapsApp.bb) {
			Settings.bbWifi = bbNetwork.getSelectedIndex() == 1;
		}
		if (Settings.allowDownload) {
			MahoMapsApp.tiles.ForceMissingDownload();
		}
		Settings.map = map.getSelectedIndex();
	}

	public void commandAction(Command c, Displayable d) {
		if (c == MahoMapsApp.back) {
			Apply();
			if (!MahoMapsApp.TryInitFSCache(false))
				return;
			// triggers settings save
			Settings.PushUsageFlag(4);
			MahoMapsApp.BringMenu();
		}
	}
}
