package sobiohazardous.minestrappolation.api.customrecipes;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class CustomSmeltingLoader {
	public static int[] smeltinIds = new int[3000];
	public static int[] smeltoutIds = new int[3000];
	public static int[] amountIds = new int[3000];	
	public static int maxAfterStringFoundValue = 19;
	
	public void loadCustomFuels(){
		File file = new File("Custom-Plugins");
		file.mkdirs();
		File file2 = new File("Custom-Plugins/"+"smelt_customFurnace.txt");
		String line;
		String input = "";
		String output = "";
		String amount = ""; 
		int arrayID=0;
		int iarrayID=0;
		int aarrayID=0;
		FilenameFilter filter = new FilenameFilter(){

			@Override
			public boolean accept(File arg0, String arg1) {
				
				return arg1.startsWith("smelt_");
			}
        	
        };
			try {
				if(!file.exists()){
					file.createNewFile();
				}
				if(!file2.exists()){
					file2.createNewFile();
				}
				
				File[] fList = file.listFiles(filter);
				
				for(int a = 0; a < fList.length;a++){
			 		FileInputStream stream = new FileInputStream(fList[a]);
				 	DataInputStream in = new DataInputStream(stream);
				 	BufferedReader br = new BufferedReader(new InputStreamReader(in));
				 	System.out.println("Loading from: "+ fList[a]);
			 		while((line = br.readLine()) !=null)
				 	{
				 		line = line.toLowerCase().replaceAll(" ", "");
				 		if(line.contains("input=")){
							input = findAlias(line.substring(line.indexOf("input=")+6,line.lastIndexOf(";", line.indexOf("input=")+maxAfterStringFoundValue)),line.substring(line.indexOf("input=")+6,line.lastIndexOf(";", line.indexOf("input=")+maxAfterStringFoundValue)));
						}else
						if(line.contains("input=") == false && line.isEmpty() == false){
							input = "1";
							System.out.println("input not found defaulting");
						}
						
						if(line.contains("output=")){
							output = findAlias(line.substring(line.indexOf("output=")+7,line.lastIndexOf(";", line.indexOf("output=")+maxAfterStringFoundValue)),line.substring(line.indexOf("output=")+7,line.lastIndexOf(";", line.indexOf("output=")+maxAfterStringFoundValue)));
						}else
						if(line.contains("output=") == false &&line.isEmpty() == false){
							output = "353";
							System.out.println("output not found defaulting");
						}
						
						if(line.contains("amount=")){
							amount = line.substring(line.indexOf("amount=")+7,line.lastIndexOf(";", line.indexOf("amount=")+11));
						}else
						if(line.contains("amount=") == false &&line.isEmpty() == false){
							amount = "1";
							System.out.println("amount not found defaulting");
						}
						
				 		
				 		
				 		if(line.isEmpty() == false){
							smeltinIds[arrayID] = Integer.parseInt(input);
							smeltoutIds[iarrayID] = Integer.parseInt(output);
							amountIds[aarrayID] = Integer.parseInt(amount);
							arrayID++;
							iarrayID++;
							aarrayID++;
						}
				 	}
			 		br.close();
			 	}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			addFuels();
		
	}
	
	private void addFuels(){
		for(int i = 0; i < smeltinIds.length;i++){
			if(smeltinIds[i] == 0 || smeltoutIds[i] == 0){
				break;
			}else{
				GameRegistry.addSmelting(smeltinIds[i], new ItemStack(smeltoutIds[i],amountIds[i],0), 1F);
			}
		}
	}
	
	public String findAlias(String alias,String other){
		String directoryName = "Custom-Plugins";
		File directory = new File("Custom-Plugins");
        String value = "0";
        boolean aliasFound = false;
       
        //get all the files from a directory
        //System.out.println("Minestrappolation: Successfuly loaded metler plugins");
        //System.out.println(directory.getAbsolutePath());
        FilenameFilter filter = new FilenameFilter(){

			@Override
			public boolean accept(File arg0, String arg1) {
				
				return arg1.startsWith("alias_");
			}
        	
        };
        
        File[] fList = directory.listFiles(filter);
        for(int i = 0; i < fList.length;i++){
        	try {
				FileInputStream stream = new FileInputStream(fList[i]);
				DataInputStream in = new DataInputStream(stream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String line;
				
				int x;
				while((line = br.readLine())!=null){
					if(line.length() == 0){
						continue;
					}
					if(line.toLowerCase().replaceAll(" ", "").contains(alias+"=")){
						aliasFound = true;
					}
					
					x = line.toLowerCase().replaceAll(" ", "").indexOf(alias+"=");
					if(x>=0){
						value = line.toLowerCase().replaceAll(" ", "").substring(alias.length()+1);
						System.out.println(alias+" - Found in: "+fList[i]);
						break;
					}
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
     
        }
        if(aliasFound == true){
        	return value;
        }else{
        	return other;
        }
        
        
    }
}
