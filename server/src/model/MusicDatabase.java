package model;

import com.google.gson.Gson;
import dfs.DFS;
import dfs.FileJson;
import dfs.FilesJson;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class MusicDatabase {
    private static MusicDatabase musicDatabase = null;
    private final String FILE_NAME = "MusicJson";
    private final String MUSICCLASS_REGEX = "(\\,?\\[?\\s+)(?=\\{\\s+\"release\")";
    private final int PAGE_SIZE = 20;
    private DFS dfs;

    private MusicDatabase(){}

    public static MusicDatabase GetInstance() {
        if(musicDatabase==null)
            musicDatabase = new MusicDatabase();
        return musicDatabase;
    }

    public MusicClass getSongByID(String songID) throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(FILE_NAME);
        Scanner scanner = new Scanner(fileInputStream).useDelimiter(MUSICCLASS_REGEX);
        while(scanner.hasNext()) {
            String token = scanner.next();
            MusicClass musicClass = new Gson().fromJson(token, MusicClass.class);
            if(musicClass.getSongID().equals(songID)) return musicClass;
        }
        return null;
    }

    public List<MusicClass> getSongs(int index) throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(FILE_NAME);
        Scanner scanner = new Scanner(fileInputStream).useDelimiter(MUSICCLASS_REGEX);
        List<MusicClass> ret = new ArrayList<>();
        for(int i = 0; i < PAGE_SIZE*index; i++) {
            if(scanner.hasNext())
                scanner.next();
            else
                return null;
        }
        for(int i = 0; i < PAGE_SIZE; i++) {
            if(scanner.hasNext()) {
                String token = scanner.next();
                MusicClass musicClass = new Gson().fromJson(token, MusicClass.class);
                ret.add(musicClass);
            } else break;
        }
        return ret;
    }

    public List<MusicClass> getSongsSearch(int index, String query) throws Exception {
        FilesJson md  = dfs.readMetaData();
        FileJson file = null;

        for( FileJson fj : md.getFile() ){
            if( fj.getName() == FILE_NAME ){
                file = fj;
                break;
            }
        }

        if ( file == null ){
            throw new Exception(FILE_NAME + " not found!");
        }

        List<MusicClass> ret = new ArrayList<>();
        query = query.toLowerCase();
        for( int page = 1; page <= file.getNumberOfPages(); page++ ){
            Scanner scanner = new Scanner( dfs.read(FILE_NAME, page) ).useDelimiter(MUSICCLASS_REGEX);

            while( scanner.hasNext() ){
                String token = scanner.next();
                if(token.endsWith("]")){
                    token = token.substring(0, token.length()-1);
                }

                try {
                    MusicClass musicClass = new Gson().fromJson(token, MusicClass.class);
                    if (musicClass.getSongTitle().toLowerCase().contains(query)) {
                        ret.add(musicClass);
                    }
                    if (musicClass.getArtistName().toLowerCase().contains(query)) {
                        ret.add(musicClass);
                    }

                }catch (Exception e){
                    System.out.println(token);
                }
            }

        }

        /*
        System.out.println("Searching...");
        FileInputStream fileInputStream = new FileInputStream(FILE_NAME);
        Scanner scanner = new Scanner(fileInputStream).useDelimiter(MUSICCLASS_REGEX);

        List<MusicClass> ret = new ArrayList<>();
        query = query.toLowerCase();

        int c = 0;
        while(scanner.hasNext()){
            String token = scanner.next();
            if(token.endsWith("]")){
                token = token.substring(0, token.length()-1);
            }
            try {
                MusicClass musicClass = new Gson().fromJson(token, MusicClass.class);
                if (musicClass.getSongTitle().toLowerCase().contains(query)) {
                    ret.add(musicClass);
                }
                if (musicClass.getArtistName().toLowerCase().contains(query)) {
                    ret.add(musicClass);
                }
                //System.out.println(c);
                c++;

            }catch (Exception e){
                System.out.println(token);
            }
            }
        System.out.println("Search completed.");
        */

        return ret.subList(index*PAGE_SIZE, (index*PAGE_SIZE)+PAGE_SIZE);
    }

    public void setDfs(DFS dfs) {
        this.dfs = dfs;
    }
}
