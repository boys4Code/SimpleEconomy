package com.boys4code.plugin;

import com.google.common.base.Converter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Main extends JavaPlugin implements Listener{

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        this.getConfig().options().copyDefaults();
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        saveYmlConfig();
    }

    @Override
    public void onLoad() {
        saveYmlConfig();
    }

    private FileConfiguration ymlConfig = null;
    private File ymlFile = null;

    public void onJoin(PlayerJoinEvent e){
        if(!(getYmlConfig().isSet("Players." + e.getPlayer().getName() + ".Money"))){
            getYmlConfig().set("Players." + e.getPlayer().getName() + ".Money", 0);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        if(cmd.getName().equals("balance")){
            if(sender instanceof Player){
                if(!(getYmlConfig().isSet("Players." + player.getName() + ".Money"))){
                    getYmlConfig().set("Players." + player.getName() + ".Money", 0);
                    player.sendMessage(ChatColor.GREEN + "Egyenleged: " + ChatColor.WHITE + getYmlConfig().get("Players." + player.getName() + ".Money").toString());
                    saveYmlConfig();
                }else{
                    player.sendMessage(ChatColor.GREEN + "Egyenleged: " + ChatColor.WHITE + getYmlConfig().get("Players." + player.getName() + ".Money").toString());
                }
            }
        }else if(cmd.getName().equals("economy")){
            if(args.length < 1){
                player.sendMessage( ChatColor.RED + "??gy haszn??ld: " + ChatColor.GREEN + "/economy" + ChatColor.GOLD + " <set/reset/remove>");
            }else if(args[0].equalsIgnoreCase("set")){
                if(args.length > 1){
                    if(getYmlConfig().isSet("Players." + args[1] + ".Money")){
                        if(args.length > 2){
                            try{
                                int money = Integer.parseInt(args[2]);
                                getYmlConfig().set("Players." + args[1] + ".Money", money);
                                saveYmlConfig();
                                player.sendMessage( ChatColor.GREEN + "Sikeresen be??ll??tottad " + ChatColor.WHITE + args[1] + ChatColor.GREEN +" egyenleg??t " + ChatColor.GOLD + money + ChatColor.GREEN + "-re.");
                            }catch (Exception e){
                                player.sendMessage(ChatColor.RED + "Sz??mot adj meg!");
                            }
                        }else{
                            player.sendMessage(ChatColor.RED + "??gy haszn??ld: " + ChatColor.GREEN + "/economy set" + ChatColor.GOLD + " <player> <??sszeg>");
                        }
                    }else{
                        player.sendMessage( ChatColor.RED+ "Ilyen j??t??kos nincs az adatb??zisban!");
                    }
                }else{
                    player.sendMessage(ChatColor.RED + "??gy haszn??ld: " + ChatColor.GREEN + "/economy set" + ChatColor.GOLD + " <player> <??sszeg>");
                }
            }else if(args[0].equalsIgnoreCase("reset")){
                if(args.length == 1){
                    getYmlConfig().set("Players." + player.getName() + ".Money", 0);
                    player.sendMessage(ChatColor.GREEN + "Sikeresen alaphelyzetbe raktad az egyenlegedet.");
                    saveYmlConfig();
                }else{
                    if(getYmlConfig().isSet("Players." + args[1] + ".Money")){
                        getYmlConfig().set("Players." + args[1] + ".Money", 0);
                        saveYmlConfig();
                        if(Bukkit.getPlayerExact(args[1]) != null){
                            player.sendMessage(ChatColor.GREEN + "Sikeresen alaphelyzetbe tetted " + ChatColor.WHITE + args[1] + ChatColor.GREEN + " j??t??kos egyenleg??t.");
                            Bukkit.getPlayerExact(args[1]).sendMessage(ChatColor.GREEN + "Alap helyzetbe lett ??ll??tva a p??nzed.");
                        }else{
                            player.sendMessage(ChatColor.GREEN + "Sikeresen alaphelyzetbe tetted " + ChatColor.WHITE + args[1] + ChatColor.GREEN + " j??t??kos egyenleg??t.");
                        }
                    }else{
                        player.sendMessage( ChatColor.RED+ "Ilyen j??t??kos nincs az adatb??zisban!");
                    }
                }
            }else if(args[0].equalsIgnoreCase("remove")){
                if(args.length > 1){
                    if(getYmlConfig().isSet("Players." + args[1] + ".Money")){
                        if(args.length > 2){
                            try {
                                int money = Integer.parseInt(args[2]);
                                int money2 = Integer.parseInt(getYmlConfig().get("Players." + args[1] + ".Money").toString());
                                money2 = money2 - money;
                                getYmlConfig().set("Players." + args[1] + ".Money", money2);
                                saveYmlConfig();
                                player.sendMessage(ChatColor.GREEN + "Sikeresen t??r??lt??l " + ChatColor.WHITE + args[1] + ChatColor.GREEN + " j??t??kos egyenleg??r??l " + ChatColor.GOLD + money + ChatColor.GREEN + " p??nzt.");
                            } catch (Exception e){
                                player.sendMessage(ChatColor.RED + "Sz??mot adj meg!");
                            }
                        }else{
                            player.sendMessage( ChatColor.RED + "??gy haszn??ld: " + ChatColor.GREEN + "/econonmy remove" +ChatColor.GOLD + " <player> <??sszeg>");
                        }
                    }else{
                        player.sendMessage( ChatColor.RED+ "Ilyen j??t??kos nincs az adatb??zisban!");
                    }
                }else{
                    player.sendMessage( ChatColor.RED + "??gy haszn??ld: " + ChatColor.GREEN + "/econonmy remove" +ChatColor.GOLD + " <player> <??sszeg>");
                }
            }else if(args[0].equalsIgnoreCase("give")){
                if(args.length > 1){
                    if(getYmlConfig().isSet("Players." + args[1] + ".Money")){
                        if(args.length > 2){
                            try {
                                int money = Integer.parseInt(args[2]);
                                int money2 = Integer.parseInt(getYmlConfig().get("Players." + args[1] + ".Money").toString());
                                money2 = money2 + money;
                                getYmlConfig().set("Players." + args[1] + ".Money", money2);
                                saveYmlConfig();
                                player.sendMessage(ChatColor.GREEN + "Sikeresen hozz??adt??l " + ChatColor.WHITE + args[1] + ChatColor.GREEN + " j??t??kos egyenleg??re " + ChatColor.GOLD + money + ChatColor.GREEN + " p??nzt.");
                            } catch (Exception e){
                                player.sendMessage(ChatColor.RED + "Sz??mot adj meg!");
                            }
                        }else{
                            player.sendMessage( ChatColor.RED + "??gy haszn??ld: " + ChatColor.GREEN + "/econonmy give" +ChatColor.GOLD + " <player> <??sszeg>");
                        }
                    }else{
                        player.sendMessage( ChatColor.RED+ "Ilyen j??t??kos nincs az adatb??zisban!");
                    }
                }else{
                    player.sendMessage( ChatColor.RED + "??gy haszn??ld: " + ChatColor.GREEN + "/econonmy give" +ChatColor.GOLD + " <player> <??sszeg>");
                }
            }
        }else if(cmd.getName().equals("pay")){
            if(args.length < 1){
                player.sendMessage(ChatColor.RED + "??gy haszn??ld: " + ChatColor.GREEN + "/pay" + ChatColor.GOLD + " <player> <??sszeg>");
            }else if(!(getYmlConfig().isSet("Players." + player.getName() + ".Money"))){
                player.sendMessage(ChatColor.RED + "Ez a j??t??kos nincs benne az adatb??zisban!");
            }else if(getYmlConfig().isSet("Players." + player.getName() + ".Money")){
                if(args.length > 1){
                    try {
                        int money2 = Integer.parseInt(args[1]);
                        int money = Integer.parseInt(getYmlConfig().get("Players." + player.getName() + ".Money").toString());
                        if (money >= money2) {
                            money = Integer.parseInt(args[1]) + Integer.parseInt(getYmlConfig().get("Players." + args[0] + ".Money").toString());
                            getYmlConfig().set("Players." + args[0] + ".Money", money);
                            int money3 = Integer.parseInt(getYmlConfig().get("Players." + player.getName() + ".Money").toString());
                            money3 = money3 - money2;
                            getYmlConfig().set("Players." + player.getName() + ".Money", money3);
                            saveYmlConfig();
                            player.sendMessage(ChatColor.GREEN + "Sikeresen elutaltad a p??nzt.");
                            if (Bukkit.getPlayerExact(args[0]) != null) {
                                Player p = Bukkit.getPlayerExact(args[0]);
                                p.sendMessage(ChatColor.GREEN + "" + money2 + " hozz??adva az egyenlegedhez.");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "Nincs el??g p??nzed!");
                        }
                    } catch (Exception e){
                        player.sendMessage(ChatColor.RED + "Sz??mot adj meg!");
                    }
                }else{
                    player.sendMessage(ChatColor.RED + "??gy haszn??ld: " + ChatColor.GREEN + "/pay " + ChatColor.GOLD + "<player> <??sszeg>");
                }
            }
        }
        return false;
    }

    public void reloadYmlCOnfig(){
        if(ymlFile == null){
            ymlFile = new File(getDataFolder(), "playerdata.yml");
        }
        ymlConfig = YamlConfiguration.loadConfiguration(ymlFile);
    }

    public FileConfiguration getYmlConfig(){
        if(ymlConfig == null){
            reloadYmlCOnfig();
        }
        return ymlConfig;
    }

    public void saveYmlConfig(){
        if(ymlConfig == null || ymlFile == null){
            return;
        }
        try{
            getYmlConfig().save(ymlFile);
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }
}

