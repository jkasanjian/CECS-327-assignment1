package dfs;
import com.google.gson.Gson;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import model.MusicClass;


/* JSON Format

{"file":
  [
     {"name":"MyFile",
      "size":128000000,
      "pages":
      [
         {
            "guid":11,
            "size":64000000
         },
         {
            "guid":13,
            "size":64000000
         }
      ]
      }
   ]
}
*/


public class DFS implements Serializable
{

    int port;
    String pathToID;
    Chord  chord;

    /**
     * Class used to create threads for searching
     */
    public class PeerSearch implements Runnable{
        ChordMessageInterface peer;
        List<MusicClass> collection;
        String targetString;
        String file;


        public PeerSearch( ChordMessageInterface peer, String file, String targetString ){
            this.peer = peer;
            this.collection = new ArrayList<>();
            this.targetString = targetString;
            this.file = file;
        }

        public void run(){
            try{
                synchronized (peer){
                    collection = peer.search(file, targetString);
                }
                System.out.println(collection.size());
            }catch(Exception e){
                System.out.println(e.getMessage());
                System.out.println(e.getStackTrace());
            }
        }

        public List<MusicClass> getCollection(){
            return collection;
        }
    }




    private long md5(String objectName)
    {
        try
        {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(objectName.getBytes());
            BigInteger bigInt = new BigInteger(1,m.digest());
            return Math.abs(bigInt.longValue());
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();

        }
        return 0;
    }



    public DFS(int port) throws Exception
    {


        this.port = port;
        long guid = md5("" + port);
        chord = new Chord(port, guid);
        Files.createDirectories(Paths.get(guid+"/repository"));
        Files.createDirectories(Paths.get(guid+"/tmp"));
        this.pathToID = guid + "/repository/ID.txt";
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                chord.leave();
            }
        });

        if(!new File(pathToID).exists()){
            writeID(1);
        }


    }


    /**
     * Join the chord
     *
     */
    public void join(String Ip, int port) throws Exception
    {
        chord.joinRing(Ip, port);
        chord.print();
    }


    /**
     * leave the chord
     *
     */
    public void leave() throws Exception
    {
        chord.leave();
    }

    /**
     * print the status of the peer in the chord
     *
     */
    public void print() throws Exception
    {
        chord.print();
    }

    /**
     * readMetaData read the metadata from the chord
     *
     */
    public FilesJson readMetaData() throws Exception
    {
        FilesJson filesJson = null;
        try {
            Gson gson = new Gson();
            long guid = md5("Metadata");

            System.out.println("GUID " + guid);
            ChordMessageInterface peer = chord.locateSuccessor(guid);
            RemoteInputFileStream metadataraw = peer.get(guid);
            metadataraw.connect();
            Scanner scan = new Scanner(metadataraw);
            scan.useDelimiter("\\A");
            String strMetaData = scan.next();
            System.out.println(strMetaData);
            filesJson = gson.fromJson(strMetaData, FilesJson.class);
        } catch (Exception ex)
        {
            filesJson = new FilesJson();
        }
        return filesJson;
    }

    /**
     * writeMetaData write the metadata back to the chord
     *
     */
    public void writeMetaData(FilesJson filesJson) throws Exception
    {
        long guid = md5("Metadata");
        ChordMessageInterface peer = chord.locateSuccessor(guid);

        Gson gson = new Gson();
        peer.put(guid, gson.toJson(filesJson));
    }

    /**
     * Change Name
     *
     */
    public void move(String oldName, String newName) throws Exception
    {
        // TODO:  Change the name in Metadata
        FilesJson md = readMetaData();
        List<FileJson> files = md.getFile();
        for ( FileJson fjson: files) {
            if( fjson.name.equals(oldName)){
                fjson.setName(newName);
                break;
            }
        }

        // Write Metadata
        md.setFile( files );
        writeMetaData(md);
    }


    /**
     * List the files in the system
     */
    public String lists() throws Exception
    {
        FilesJson md = readMetaData();
        List<FileJson> files = md.getFile();
        String listOfFiles = "";
        for ( FileJson fjson : files) {
            listOfFiles += (fjson.getName() + "\n");
        }
        return listOfFiles;
    }

    /**
     * create an empty file
     *
     * @param fileName Name of the file
     */
    public void create(String fileName) throws Exception
    {
        // Retrieving Metadata as FilesJson Object
        FilesJson metadata = readMetaData();

        // Creating JSONFile object for new file
        FileJson newFile = new FileJson(fileName);

        // Appending new JSONFile object into metadata files list
        metadata.addFile(newFile);

        // Entering new file entry into Metadata
        writeMetaData(metadata);

    }

    /**
     * delete file
     *
     * @param fileName Name of the file
     */
    public void delete(String fileName) throws Exception
    {
        FilesJson md = readMetaData();

        ArrayList<Long> GUIDs = md.getGUIDs(fileName);
        md.deleteFile(fileName);
        writeMetaData(md);

        for(Long guid : GUIDs){
            ChordMessageInterface peer = chord.locateSuccessor(guid);
            peer.delete(guid);
        }
    }

    /**
     * Read block pageNumber of fileName
     *
     * @param fileName Name of the file
     * @param pageNumber number of block.
     */
    public RemoteInputFileStream read(String fileName, int pageNumber) throws Exception
    {
        RemoteInputFileStream dataraw = null;

        while(!proposeRead(fileName)){
            System.out.println("Arriving at consensus...");

        }

        FilesJson md = readMetaData();
        List<FileJson> files = md.getFile();
        FileJson target = new FileJson();   // initialized, but will be replaced
        String listOfFiles = "";
        for ( FileJson fjson: files) {
            if (fjson.name.equals(fileName)) {
                target = fjson;
                break;
            }
        }
        target.setReadTS(java.time.LocalDateTime.now().toString());
        // reads first replication
        Long guid = target.getPages().get(pageNumber-1).getGuids().get(0);
        ChordMessageInterface peer = chord.locateSuccessor(guid);
        dataraw = peer.get(guid);
        dataraw.connect();
        return dataraw;


    }


    public Long getHighestGUID(String fileName) throws RemoteException, Exception{

        FilesJson md = readMetaData();
        int currentID = getLatestID();

        ArrayList<Long> guids = md.getGUIDs(fileName);

        Long targetID = 0L;
        int maxID = 0;

        for(Long guid : guids){
            ChordMessageInterface peer = chord.locateSuccessor(guid);
            if(peer.getTranID() > maxID){
                targetID = guid;
                maxID = peer.getTranID();
            }

        }

        return targetID;

    }


    /**
     * Add a page to the file
     *
     * @param fileName Name of the file
     * @param dataFile filename of remote .
     */
    public void append(String fileName, String dataFile) throws Exception
    {
        RemoteInputFileStream data1 = new RemoteInputFileStream(dataFile);
        RemoteInputFileStream data2 = new RemoteInputFileStream(dataFile);
        RemoteInputFileStream data3 = new RemoteInputFileStream(dataFile);

        FilesJson filesJson = readMetaData();
        List<FileJson> fileJsonList = filesJson.getFile();
        for(FileJson fileJson : fileJsonList) {
            if(fileJson.name.equals(fileName)) {
                ArrayList<PageJson> pageJsonList = fileJson.pages;
                PageJson pageJson = new PageJson();

                long guid1 = md5(fileJson.name + pageJson.creationTS + "1");
                long guid2 = md5(fileJson.name + pageJson.creationTS + "2");
                long guid3 = md5(fileJson.name + pageJson.creationTS + "3");
                ArrayList<Long> guids = new ArrayList<>();
                guids.add(guid1);
                guids.add(guid2);
                guids.add(guid3);
                pageJson.setGuids(guids);

                pageJsonList.add(pageJson);
                fileJson.pages = pageJsonList;
                fileJson.setNumberOfPages(fileJson.getNumberOfPages()+1);
                writeMetaData(filesJson);

                chord.locateSuccessor(guid1).put(guid1, data1);
                System.out.println("ADDED replication 1");

                chord.locateSuccessor(guid2).put(guid2, data2);
                System.out.println("ADDED replication 2");

                chord.locateSuccessor(guid3).put(guid3, data3);
                System.out.println("ADDED replication 3");




                break;
            }
        }
    }

    public void updatePage(String fileName, int pageNumber, String data) throws Exception {
        FilesJson metadata = readMetaData();
        List<FileJson> fileJsonList = metadata.getFile();

        boolean isSuccessful = false;

        while(!isSuccessful){
            for ( FileJson fileJson: fileJsonList) {
                if (fileJson.name.equals(fileName)) {
                    ArrayList<PageJson> pageJsonList = fileJson.pages;
                    ArrayList<Long> guids = fileJson.getPages().get(pageNumber-1).getGuids();
                    isSuccessful = proposeWrite(guids, data);

                }
            }
        }
        System.out.println("success");

    }


    /**
     * Method to search by song name or artist
     * @param fileName name of file to be searched (MusicJson)
     * @param targetString query to be searched
     * @return list of MusicClass that contain the query in the name or artist
     * @throws Exception
     */
    public List<MusicClass> search( String fileName, String targetString ) throws Exception{
        FilesJson md = readMetaData();
        FileJson music_file = null;
        List<FileJson> files = md.getFile();
        for ( FileJson fjson: files ) {
            if( fjson.name.equals("MusicJson")){
                music_file = fjson;
                break;
            }
        }

        Thread [] threads = null;
        PeerSearch [] peers = null;
        ArrayList<MusicClass> ret = new ArrayList<>();
        if ( music_file == null ){
            throw new Exception("NOT FOUND!");
        }else{

            threads = new Thread[ music_file.getNumberOfPages() ];
            peers   = new PeerSearch[ music_file.getNumberOfPages() ];


            for (int page = 1; page <= music_file.getNumberOfPages(); page++) {
                Long guid = music_file.getPages().get(page - 1).getGuids().get(0);
                ChordMessageInterface peer = chord.locateSuccessor(guid);
                peers[page - 1] = new PeerSearch(peer, guid.toString(), targetString);
                threads[page - 1] = new Thread(peers[page - 1]);
                threads[page - 1].run();
            }

            for( Thread thread : threads ){
                thread.join();
            }

            for( PeerSearch peer : peers ){
                ret.addAll( peer.getCollection() );
                System.out.println(ret.size());

            }
        }

        return ret;
    }


    public int getLatestID() throws FileNotFoundException {
        // read file and return latest ID
        Scanner sc = new Scanner(new File(this.pathToID));
        int ret = sc.nextInt();
        sc.close();
        return ret;
    }


    public void writeID(int ID) throws FileNotFoundException, UnsupportedEncodingException {
        // writes ID to file
        PrintWriter writer = new PrintWriter(this.pathToID, "UTF-8");
        writer.println((ID));
        writer.close();
    }


    public boolean proposeRead(String fileName) throws RemoteException, Exception {
        // flag either 1 or 0
        FilesJson md = null;
        RemoteInputFileStream ret;

        int currentID = getLatestID();


        int votes = 0;
        md = readMetaData();

        ArrayList<Long> guids = md.getGUIDs(fileName);

        for(Long guid : guids){
            ChordMessageInterface peer = chord.locateSuccessor(guid);
            votes += peer.promise(currentID) ? 1 : 0;
        }
        currentID ++;
        writeID(currentID);

        return(votes >= 2); // TODO: not hardcode
    }

    public boolean proposeWrite(ArrayList<Long> guids, String newData) throws RemoteException, Exception {

        FilesJson md = null;
        RemoteInputFileStream ret;


        int votes = 0;
        md = readMetaData();
        int currentID = getLatestID();


        for (Long guid : guids) {
            ChordMessageInterface peer = chord.locateSuccessor(guid);
            votes += peer.promise(currentID, newData, guid) ? 1 : 0;

        }

        if(votes >= 2){
            for(Long guid : guids){
                ChordMessageInterface peer = chord.locateSuccessor(guid);
                peer.commit(guid, currentID);
            }
        }
        return(votes >= 2);

    }



}

