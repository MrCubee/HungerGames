package fr.mrcubee.pluginutil.spigot.annotations.command;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
public enum CommandParameterTypes {
	
	STRING("String", "java.lang.String"),
	NUMBER("Number", "java.lang.Number"),
	MATERIAL_NAME("MaterialName", "org.bukkit.Material"),
	MATERIAL_ID("MaterialID", "org.bukkit.Material"),
	ENTITY_TYPE("EntityType", "org.bukkit.entity.EntityType"),
	PLAYER_NAME("PlayerName", "org.bukkit.entity.Player"),
	PLAYER_UUID("PlayerUUID", "org.bukkit.entity.Player"),
	CHATCOLOR("ChatColor", "org.bukkit.ChatColor"),
	DYECOLOR("DyeColor", "org.bukkit.DyeColor"),
	VOID("VOID", "VOID");
	
	private String typeName;
	private String className;
	
	private CommandParameterTypes(String typeName, String className) {
		this.typeName = typeName;
		this.className = className;
	}
	
	private Player getPlayerUUID(String value) {
		UUID uuid;
		
		try {
			uuid = UUID.fromString(value);
			if (uuid != null)
				return Bukkit.getPlayer(uuid);
		} catch (Exception e) {}
		return null;
	}
	
	private Material getMaterialName(String value) {
		if (value == null || value.isEmpty())
			return null;
		for (Material material : Material.values())
			if (material.toString().equalsIgnoreCase(value))
				return material;
		return null;
	}
	
	private Material getMaterialID(String value) {
		int id = 0;
		
		if (value == null || value.isEmpty())
			return null;
		try {
			id = Integer.parseInt(value);
		} catch (Exception e) {
			return null;
		}
		for (Material material : Material.values())
			if (material.getId() == id)
				return material;
		return null;
	}
	
	private EntityType getEntityType(String value) {
		if (value == null || value.isEmpty())
			return null;
		for (EntityType entityType : EntityType.values())
			if (entityType.getName().equalsIgnoreCase(value))
				return entityType;
		return null;
	}
	
	private ChatColor getChatColor(String value) {
		if (value == null || value.isEmpty())
			return null;
		for (ChatColor chatColor : ChatColor.values())
			if (chatColor.toString().equalsIgnoreCase("value"))
				return chatColor;
		return null;
	}
	
	private DyeColor getDyeColor(String value) {
		if (value == null || value.isEmpty())
			return null;
		for (DyeColor dyeColor : DyeColor.values())
			if (dyeColor.toString().equalsIgnoreCase(value))
				return dyeColor;
		return null;
	}
	
	public Object getValue(String value) {
		if (value == null || value.isEmpty())
			return null;
		switch (typeName) {
		case "String" :
			return value;
		case "MaterialName" :
			return getMaterialName(value);
		case "MaterialID" :
			return getMaterialID(value);
		case "EntityType" :
			return getEntityType(value);
		case "PlayerName" :
			return Bukkit.getPlayer(value);
		case "PlayerUUID" :
			return getPlayerUUID(value);
		case "ChatColor" :
			return getChatColor(value);
		case "DyeColor" :
			return getDyeColor(value);
		default:
			return null;
		}
	}
	
	public Object[] getAllValue() {
		Object[] result;
		
		switch (typeName) {
		case "MaterialName" :
			return Material.values();
		case "MaterialID" :
			return Material.values();
		case "EntityType" :
			return EntityType.values();
		case "PlayerName" :
			result = Bukkit.getOnlinePlayers().toArray();
			if (result == null || result.length < 1)
				return null;
			return result;
		case "PlayerUUID" :
			result = Bukkit.getOnlinePlayers().toArray();
			if (result == null || result.length < 1)
				return null;
			return result;
		case "ChatColor" :
			return ChatColor.values();
		case "DyeColor" :
			return DyeColor.values();
		default:
			return null;
		}
	}
	
	public String getClassName() {
		return className;
	}

}
