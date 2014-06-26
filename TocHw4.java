import java.io.*;
import org.json.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TocHw4 {
        public static void main(String[] args){
        	try {  
                URL url = new URL(args[0]);
                InputStream InpStr=url.openStream();
                InputStreamReader InpStrRead=new InputStreamReader(InpStr,"UTF-8");

                JSONTokener Token=new JSONTokener(InpStrRead);
                JSONArray Array = new JSONArray(Token);

                Pattern factor1=Pattern.compile(".*大道|.*路|.*街");
                Pattern factor2=Pattern.compile(".*巷");

                ArrayList<String> Road=new ArrayList<String>();
                for(int c=0;c<Array.length();c++){
                        Matcher get1=factor1.matcher(Array.getJSONObject(c).getString("土地區段位置或建物區門牌"));
                        Matcher get2=factor2.matcher(Array.getJSONObject(c).getString("土地區段位置或建物區門牌"));
                        if(get1.find()){
                                if(!Road.contains(get1.group())){
                                        Road.add(get1.group());
                                }
                        }
                        else if(get2.find()){
                                if(!Road.contains(get2.group())){
                                        Road.add(get2.group());
                                }
                        }
                }
                int count=Road.size();
                ArrayList<Integer>[] Year = new ArrayList[count];
                ArrayList<Integer>[] Price = new ArrayList[count];
                for(int c=0;c<count;c++){
                        ArrayList<Integer> list1=new ArrayList<Integer>();
                        ArrayList<Integer> list2=new ArrayList<Integer>();
                        Year[c]=list1;
                        Price[c]=list2;
                }
                for(int c=0;c<Array.length();c++){
                        Matcher get1=factor1.matcher(Array.getJSONObject(c).getString("土地區段位置或建物區門牌"));
                        Matcher get2=factor2.matcher(Array.getJSONObject(c).getString("土地區段位置或建物區門牌"));
                        if(get1.find()){
                                String temp=get1.group();
                                int order=Road.indexOf(temp);
                                int tempYear=Array.getJSONObject(c).getInt("交易年月");
                                int tempPrice=Array.getJSONObject(c).getInt("總價元");
                                if(!Year[order].contains(tempYear)) Year[order].add(tempYear);
                                if(!Price[order].contains(tempPrice)) Price[order].add(tempPrice);
                        }
                        else if(get2.find()){
                                String temp=get2.group();
                                int order=Road.indexOf(temp);
                                int tempYear=Array.getJSONObject(c).getInt("交易年月");
                                int tempPrice=Array.getJSONObject(c).getInt("總價元");
                                if(!Year[order].contains(tempYear)) Year[order].add(tempYear);
                                if(!Price[order].contains(tempPrice)) Price[order].add(tempPrice);
                        }
                }

                int MaxTrade=0;
                ArrayList<Integer> Toplist=new ArrayList<Integer>();
                for(int c=0;c<count;c++){
                        if(Year[c].size()>MaxTrade){
                                MaxTrade=Year[c].size();
                                Toplist.clear();
                                Toplist.add(c);
                        }
                        else if(Year[c].size()==MaxTrade){
                                Toplist.add(c);
                        }
                }

                for(int c=0;c<Toplist.size();c++){
                        Collections.sort(Price[Toplist.get(c)]);
                        System.out.println( Road.get(Toplist.get(c)) + ", 最高成交價: " + Price[Toplist.get(c)].get(Price[Toplist.get(c)].size()-1) + ", 最低成交價: " + Price[Toplist.get(c)].get(0) );
                }

	        } catch (MalformedURLException e) {  
	            e.printStackTrace();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        } catch (JSONException e) {
	        	e.printStackTrace();  
	        }
        }
}