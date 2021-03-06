package it.planetgeeks.mclauncher.utils;

import it.planetgeeks.mclauncher.Launcher;
import it.planetgeeks.mclauncher.LauncherLogger;
import it.planetgeeks.mclauncher.LauncherProperties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author PlanetGeeks
 * 
 */

public class LanguageUtils
{

	private static ArrayList<LanguagePack> langs = new ArrayList<LanguagePack>();
	private static LanguagePack currentPack;
	private static LanguagePack latest = null;

	public static void loadLanguages()
	{
		currentPack = null;
		langs = new ArrayList<LanguagePack>();

		try
		{
			InputStream input = Launcher.getResources().getInputStream("languages/languages.list");
			if (input != null)
			{
				BufferedReader br = new BufferedReader(new InputStreamReader(input));

				String readed = br.readLine();
				while (readed != null)
				{
					if (readed.contains("="))
					{
						LanguagePack pack = loadLangPack(readed.split("=")[0], readed.split("=")[1]);
						langs.add(pack);
					}
					readed = br.readLine();
				}
				br.close();

				String defaultLang = LauncherProperties.getProperty("language");

				for (int i = 0; i < langs.size(); i++)
				{
					LanguagePack current = langs.get(i);
					if (current.packName.equals(defaultLang))
					{
						currentPack = current;
						break;
					}
				}

				if (currentPack == null)
				{
					for (int i = 0; i < langs.size(); i++)
					{
						LanguagePack current = langs.get(i);
						if (current.isDefault)
						{
							currentPack = current;
							break;
						}
					}

					if (currentPack == null && langs.size() > 0)
					{
						currentPack = langs.get(0);
					}

					LauncherProperties.modifyProperty("language", currentPack.packName);
				}

				input.close();

			}
			else
			{
				LauncherLogger.log(LauncherLogger.SEVERE, "Error on loading languages list! Does languages.list file exist in launcher.jar?");
			}

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static LanguagePack loadLangPack(String languageName, String fileName)
	{
		try
		{
			InputStream input = Launcher.getResources().getInputStream("languages/" + fileName);
			if (input != null)
			{
				BufferedReader br = new BufferedReader(new InputStreamReader(input));
				LanguagePack langPack = new LanguagePack(languageName);
				String readed = br.readLine();
				while (readed != null)
				{
					if (readed.startsWith("!"))
					{
						if (readed.contains("isDefault"))
						{
							langPack.isDefault = readed.split("=")[1].equals("true") ? true : false;
						}
					}
					else if (readed.contains("="))
					{
						langPack.translations.put(readed.split("=")[0], readed.split("=")[1]);
					}
					readed = br.readLine();
				}
				LauncherLogger.log(LauncherLogger.INFO, "Loaded languagePack '" + languageName + "'");
				return langPack;
			}
			else
			{
				LauncherLogger.log(LauncherLogger.SEVERE, "Could not load '" + fileName + "' languagePack!");
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public static String getTranslated(String key)
	{
		if (currentPack != null && currentPack.translations != null && currentPack.translations.containsKey(key))
		{
			return currentPack.translations.get(key);
		}
		return key;
	}

	public static LanguagePack getLatest()
	{
		return latest;
	}

	public static String getKey(String translated, LanguagePack pack)
	{

		if (pack != null && pack.translations != null && pack.translations.containsValue(translated))
		{
			Iterator<String> it = pack.translations.keySet().iterator();
			while (it.hasNext())
			{
				String key = it.next();
				if (pack.translations.get(key).equals(translated))
				{
					return key;
				}
			}
		}
		return translated;
	}

	public static String[] getNames()
	{
		String[] str = new String[langs.size()];

		for (int i = 0; i < str.length; i++)
		{
			str[i] = langs.get(i).packName;
		}

		return str;
	}

	public static void setLanguage(int index)
	{
		latest = currentPack;
		currentPack = langs.get(index) != null ? langs.get(index) : currentPack;
	    Launcher.languageChanged();
	    LauncherProperties.modifyProperty("language", currentPack.packName);
	}

	public static int getLangIndex(String str)
	{
		String langs[] = LanguageUtils.getNames();
		
		for(int i = 0; i < langs.length; i++)
		{
			if(str.equals(langs[i]))
				return i;
		}
			
		return 0;
	}

	public static int getCurrentLangIndex()
	{
		for (int i = 0; i < langs.size(); i++)
		{
			if (currentPack.packName.equals(langs.get(i).packName))
			{
				return i;
			}
		}
		return 0;
	}
}
