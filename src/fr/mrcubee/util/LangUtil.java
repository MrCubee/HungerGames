package fr.mrcubee.util;

import fr.mrcubee.langlib.Lang;
import org.bukkit.entity.Player;

public class LangUtil {

    public static void updatePlayerLocale(Player player, String lang) {
        if (player == null || lang == null)
            return;
        if (lang.startsWith("fr"))
            Lang.setPlayerLang(player, "FR_fr");
        else if (lang.startsWith("en"))
            Lang.setPlayerLang(player, "EN_us");
        else if (lang.startsWith("zh"))
            Lang.setPlayerLang(player, "ZH_zh");
        else
            Lang.setPlayerLang(player, null);
    }

}
