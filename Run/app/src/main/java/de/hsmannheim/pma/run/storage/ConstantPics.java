package de.hsmannheim.pma.run.storage;

/**
 * Created by aaron on 09.06.17.
 * Für die einfache Bilderzuordnung!
 */

import de.hsmannheim.pma.run.R;

public class ConstantPics {

    public static int getPicId(String picUrl) {
        if (picUrl==null)return R.drawable.shoes;
        switch (picUrl) {
            case "Quadratekid":
                return R.drawable.quadratekid;
            case "Buschkind":
                return R.drawable.buschkind;
            case "Ehrensache":
                return R.drawable.ehrensache;
            case "Hafenkönig":
                return R.drawable.hafenkoenig;
            case "Luises Liebling":
                return R.drawable.luise;
            case "shoes":
                return R.drawable.shoes;
            case "fernsehturm":
                return R.drawable.fernsehturm;
            case "street":
                return R.drawable.street;
            default:
                return R.drawable.shoes;
            }
        }
    public static int getPicTropyId(String picUrl) {
        if (picUrl==null)return R.drawable.trophy_shoes;
        switch (picUrl) {
            case "Quadratekid":
                return R.drawable.trophy_quadrate;
            case "Buschkind":
                return R.drawable.trophy_buschkind;
            case "Ehrensache":
                return R.drawable.trophy_ehrensache;
            case "Hafenkönig":
                return R.drawable.trophy_hafenkoenig;
            case "Luises Liebling":
                return R.drawable.trophy_luise;
            case "shoes":
                return R.drawable.trophy_shoes;
            case "fernsehturm":
                return R.drawable.trophy_fernsehturm;
            case "street":
                return R.drawable.trophy_street;
            default:
                return R.drawable.trophy_shoes;
        }
    }

    /*
    Name: Quadratekid
    Location: Innenstadt
    Finishtext Zwischen Filmriss und City Döner macht Dir so schnell keiner was vor.

            Name: Buschkind
    Location: Jungbusch
    Finishtext: Hagestolz, Nelson, Onkel Otto, No. 6 und dann noch zum Zapfenstreich ins Rhodos? Kein Problem für Dich.

    Name: Ehrensache
    Location: Innenstadt
    Finishtext: Kurfürst Karl Theodor hätte seinen Hut vor Dir gezogen.

            Name: Hafenkönig
    Location: Jungbusch
    Finishtext: Noch eine Trophäe geangelt.

    Name: Luises Liebling
    Location: Schwetzingerstadt
    Finishtext: Luise von Baden hätte Dir für diese Leistung eine Gondoletta-Freikarte geschenkt.
*/
}
