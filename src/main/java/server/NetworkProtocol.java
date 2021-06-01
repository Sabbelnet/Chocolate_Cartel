package server;

public enum NetworkProtocol {

    /**
     * Usage: first check if the value you want to check exists:
     * if (NetworkProtocol.enumContainsValue(*Value*)) { ..
     *
     * Then make NetworkProtocol with your value:
     * NetworkProtocol NameOfValue = NetworkProtocol.valueOf(*Value*);
     */

    /**
     * SEPARATOR
     * separiert Parameter für Server-Client-Kommunikation
     */
    SEPARATOR { public String toString() { return "%"; } },
    /**
     * END
     * kennzeichnet Ende von Mitteilung
     */
    END { public String toString() { return "\n"; } },

    // Client commands
    /**
     * BUILD (ruleCheck an Server)
     * 1. param: --Gebäude gemäss enum--
     * 2. param: int Änderung der Stufe --(1 für Bau, -1 für Abriss)--
     */
    BUILD { public String toString() { return "BUILD"; } },
    /**
     * BOOST
     * 1. param: Art des booster gemäss Enum
     */
    BOOST { public String toString() { return "BOOST"; } },
    /**
     * CHATO (Schicke Chatnachricht)
     * 1. param: --"BC" für Broadcast, Username für Direktnachricht, "GRP" für Gruppenchat, "LOBBY" für lobby--
     * 2. param: Nachricht
     */
    CHATO { public String toString() { return "CHATO"; } },
    /**
     * SELTO (Verkaufsangebot an Server schicken)
     * 1. param: --Ware-- (SUGAR, CACAO, MILK, CHOCO)
     * 2. param: --Menge-- (integer) (positiv für verkauf an server, negativ für einkauf)
     * 3. param: --Preis-- (integer) preis pro 1 einheit
     */
    SELTO { public String toString() { return "SELTO"; } },
    /**
     * SPYTO (Spion schicken)
     * 1. param: --Username--
     */
    SPYTO { public String toString() { return "SPYTO"; } },
    /**
     * SABOT (sabotage)
     * 1. param: --Username--
     * 2. param: --Gebäude gemäss Enum--
     */
    SABOT { public String toString() { return "SABOT"; } },
    /**
     * LOGIN (login mit Name)
     * 1. param: --username--
     */
    LOGIN { public String toString() { return "LOGIN"; } },
    /**
     * LOGOT (logout mit Namensfreigabe)
     */
    LOGOT { public String toString() { return "LOGOT"; } },
    /**
     * GROUP (Add user to group)
     * 1. param: --"NEW" für erstelle neue Gruppe, "ADD" für füge Spieler hinzu, "JOIN" um gruppeneinladung anzunehmen, "LEAVE" zum verlassen--
     * 2. param: --Gruppenname für NEW und JOIN, Spielername für ADD--
     */
    GROUP { public String toString() { return "GROUP"; } },
    /**
     * GAMGO (starte neues spiel für die gruppe)
     * 1. param: --int Gewinnbetrag--
     * 2. param: --int Spiellänge in Monaten--
     * 3. param: --int Monatslänge in Sekunden--
     */
    GAMGO { public String toString() { return "GAMGO"; } },
    /**
     * GETPL (request spielerliste aller spieler für Achievement product: Player List)
     */
    GETPL { public String toString() { return "GETPL"; } },
    /**
     * GETGL request gruppenliste aller gruppen mit status
     */
    GETGL { public String toString() { return "GETGL"; } },
    /**
     * GETHS request top three highscores
     */
    GETHS { public String toString() { return "GETHS";} },
    /**
     * PING (checkt ob der Server noch Antwortet)
     */
    PING { public String toString() { return "PING"; } },
    /**
     * PONG (Antwort auf PING)
     */
    PONG { public String toString() { return "PONG"; } },


    // Server Commands
    /**
     * UPDAT (sendet Spieler und Punkteliste)
     *
     * 1. param: --"LOBBY" oder "GROUP"-- Lobby für spieler ohne gruppe, group für gruppenmitgliedern
     * 2. param: --spieler,punkte" Spieler 1-n und Punktezahl der Spieler 1-n(nur bei "GRUPPE") als Argumente 1-n--
     */
    UPDAT { public String toString() { return "UPDAT"; } },
    /**
     * PRDUP (PRoDuction UPdate - sendet monatliches produktionsupdate an spieler)
     * (Reihenfolge: cash, zucker, cacao, milch, choco)
     * 1. param: --Monat--
     * 2. param: --Menge, verdiente Menge im letzten Monat--
     * 3. param: --Menge, verkaufte Menge im letzten Monat, Preis--
     * 4. param: --Menge, Verkaufte Menge im letzten Monat, Preis--
     * 5. param: --Menge, Verkaufte Menge im letzten Monat, Preis--
     * 6. param: --Menge, Verkaufte Menge im letzten Monat, Preis--
     */
    PRDUP { public String toString() { return "PRDUP"; } },
    /**
     * ECOUP (ECOnomy UPdate)
     * 1. param: --newSellPrice, newBuyPrice, maxSellPrice, minSellPrice, durchschnitt, maxBuyPrice, minBuyPrice, Durchschnitt,sellLimit, BuyLimit--
     * 2. param: --newSellPrice, newBuyPrice, maxSellPrice, minSellPrice, durchschnitt, maxBuyPrice, minBuyPrice, Durchschnitt,sellLimit, BuyLimit--
     * 3. param: --newSellPrice, newBuyPrice, maxSellPrice, minSellPrice, durchschnitt, maxBuyPrice, minBuyPrice, Durchschnitt,sellLimit, BuyLimit--
     * 4. param: --newSellPrice, newBuyPrice, maxSellPrice, minSellPrice, durchschnitt, maxBuyPrice, minBuyPrice, Durchschnitt,sellLimit, BuyLimit--
     */
    ECOUP { public String toString() { return "ECOUP"; } },
    /**
     * BLDCK (Buildcheck, sendet antwort auf BUILD)
     * 1. param: --Gebäude gemäss Enum--
     * 2. param: --neues Level-- (Bei illegaler Bauaktion wird das level nicht verändert)
     * 3. param: --neues vermögen--
     */
    BLDCK { public String toString() { return "BLDCK"; } },
    /**
     * BSTCK (bestötige boosterkauf an client)
     * 1. param: --Booster gemäss enum-- gekaufter booster wenn Legal, sonst default
     * 2. param: --neues Kapital-- (altes kapital wenn nichts gekauft)
     */
    BSTCK { public String toString() { return "BSTCK"; } },
    /**
     * CHATI (Nachricht, die dem Player angezeigt werden soll)
     * 1. param: --"BC" für Broadcast, "WSP" für Direktnachricht, "GRP" für Gruppenchat, "LOBBY" für lobby--
     * 2. param: --nachricht--
     * 3. param: --username (nur bei "msg")--
     */
    CHATI { public String toString() { return "CHATI"; } },
    /**
     * SPYCK (Spyback an Sender von Empfänger)
     * 1 param: --name des ausgespähten spielers--
     * 2. param: --SUGARFARM, gebäudelevel--
     * 3. param: --CACAOFARM, gebäudelevel--
     * 4. param: --STABLE, gebäudelevel--
     * 5. param: --FACTORY, gebäudelevel--
     * 6. param: --LAB, gebäudelevel--
     * 7. param: --WAREHOUSE, gebäudelevel--
     */
    SPYCK { public String toString() { return "SPYCK"; } },
    /**
     * SABCK (rückmeldung an Sabotierten)
     * 1. param: --name des saboteurs--
     * 2. param: --sabotiertes gebäude--
     * 3. param: --neues Gebäudelevel--
     */
    SABCK { public String toString() { return "SABCK"; } },
    /**
     * SELCK (Verkaufsmenge bestätigen an client)
     * 1. param: --Rohstoff--
     * 2. param: --Menge-- (0 falls nicht genügend rohstoffe oder Preis viel zu hoch/tief)
     */
    SELCK { public String toString() { return "SELCK"; } },
    /**
     * NAMCK (Name wird zur Kontrolle an Client zurückgesendet)
     * 1. param: --username--
     */
    NAMCK { public String toString() { return "NAMCK"; } },
    /**
     * GRPIN (Sende Gruppeneinladung zu Spieler)
     * 1. param: --username--
     * 2. param: --groupname--
     */
    GRPIN { public String toString() { return "GRPIN"; } },
    /**
     * GAMGO (bestätige spielstart an alle clients)
     * 1. param: --int Gewinnbetrag--
     * 2. param: --int Spiellänge in Monaten--
     * 3. param: --int Monatslänge in Sekunden--
     */
    // bei Client aufgeführt
    /**
     * GAMND (beende Spiel)
     * 1. param: --eigenes Kapital--
     */
    GAMND { public String toString() { return "GAMND"; } },
    /**
     * GIVPL sendet liste aller spieler an client
     * 1. param: --Spieler 1--
     * 2. param: --Spieler 2--
     * ..
     * n. param: --Spieler n--
     */
    GIVPL { public String toString() { return "GIVPL"; } },
    /**
     * GIVGL sendet Liste aller Gruppen an client
     * 1. param: --gruppenname1,status,spieler1,spielerN..-- mögliche stati: WAITING,RUNNING,FINISHED
     * ..
     * n. param: --gruppenname n,status,spieler1,spielerN..--
     */
    GIVGL { public String toString() { return "GIVGL"; } },
    /**
     * GIVHS sendet die drei besten Highscores
     * 1. param: --Name1, Score1--
     * 2. param: --Name2, Score2--
     * 3. param: --Name3, Score3--
     */
    GIVHS { public String toString() { return "GIVHS";} },
    /**
     * DEFLT (Sendet Fehler an Client, falls dieser falsche Eingabe gemacht hat)
     */
    DEFLT { public String toString() { return "DEFLT"; } };
    /**
     * PING (checkt ob der Client noch Antwortet)
     */
    // bereits bei Client aufgeführt


    private final String command;

    NetworkProtocol(final String command) {
        this.command = command;
    }

    NetworkProtocol() {
        command = null;
    }

    /**
     * check if this enum contains a certain value
     * @param value value to check
     * @return true if value is contained, false else
     */
    public static boolean enumContainsValue(String value)
    {
        for (NetworkProtocol networkProtocol : NetworkProtocol.values())
        {
            if (networkProtocol.name().equals(value))
            {
                return true;
            }
        }

        return false;
    }
}
