package it.planetgeeks.mclauncher.modpack;

import java.util.ArrayList;

public class ModPack
{
    public String mcVersion;
    public String packName;
    public String packOwner;
    public String packServerLink;
    public ArrayList<String> modList = new ArrayList<String>();

    public ModPack(String mcVersion, String packName, String packOwner, String packServerLink)
    {
    	this.mcVersion = mcVersion;
    	this.packName = packName;
    	this.packOwner = packOwner;
    	this.packServerLink = packServerLink;
    }
    
    public ModPack(String mcVersion, String packName, String packOwner)
    {
    	this.mcVersion = mcVersion;
    	this.packName = packName;
    	this.packOwner = packOwner;
    }
    
    public void setPackServerLink(String url)
    {
    	this.packServerLink = url;
    }
    
    public void setPackName(String packName)
    {
    	this.packName = packName;
    }
    
    public void setPackOwner(String packOwner)
    {
    	this.packOwner = packOwner;
    }
    
    public void setMcVersion(String mcVersion)
    {
    	this.mcVersion = mcVersion;
    }
    
    public boolean containMod(String str)
    {
    	for(int i = 0; i < modList.size(); i++)
    	{
    		if(modList.get(i).contains(str))
    		{
    			return true;
    		}
    	}
    	
    	return false;
    }
}