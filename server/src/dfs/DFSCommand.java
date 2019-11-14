package dfs;

import java.io.*;
import java.util.Scanner;

import com.google.gson.*;
import com.google.gson.stream.*;




public class DFSCommand
{
    DFS dfs;
        
    public DFSCommand(int p, int portToJoin) throws Exception {
        dfs = new DFS(p);
        
        if (portToJoin > 0)
        {
            System.out.println("Joining "+ portToJoin);
            dfs.join("127.0.0.1", portToJoin);            
        }
        
        BufferedReader buffer=new BufferedReader(new InputStreamReader(System.in));
        String line = buffer.readLine();  
        while (!line.equals("quit"))
        {
            String[] result = line.split("\\s");
            if (result[0].equals("join")  && result.length > 1)
            {
                dfs.join("127.0.0.1", Integer.parseInt(result[1]));     
            }
            if (result[0].equals("print"))
            {
                dfs.print();     
            }
            if (result[0].equals("ls"))
            {
                dfs.lists();     
            }
            
            if (result[0].equals("leave"))
            {
                dfs.leave();     
            }

            if (result[0].equals("touch"))  // creates a new file
            {
                dfs.create(result[1]);  // next argument will have filename
            }

            if (result[0].equals("delete"))  // deletes a file
            {
                System.out.println("Enter name of file to be deleted: ");
                String fileName = buffer.readLine();
                dfs.delete(fileName);
            }

            if (result[0].equals("append")){
                dfs.append(result[1], new RemoteInputFileStream(result[2]));
            }

            if (result[0].equals("read"))  // reads a page from a file
            {
                RemoteInputFileStream data = dfs.read(result[1], Integer.parseInt(result[2]));
                data.connect();
                Scanner scan = new Scanner(data);
                scan.useDelimiter("\\A");
                String strMetaData = scan.next();
                System.out.println(strMetaData);
            }



            line=buffer.readLine();  
        }
            // User interface:
            // join, ls, touch, delete, read, tail, head, append, move
    }
    
    static public void main(String args[]) throws Exception
    {
        Gson gson = new Gson();
//        RemoteInputFileStream in = new RemoteInputFileStream("music.json", false);
//        in.connect();
//        Reader targetReader = new InputStreamReader(in);
//        JsonReader jreader = new  JsonReader(targetReader);
        //Music[] music = gson.fromJson(jreader, Music[].class);
        
        if (args.length < 1 ) {
            throw new IllegalArgumentException("Parameter: <port> <portToJoin>");
        }
        if (args.length > 1 ) {
            DFSCommand dfsCommand=new DFSCommand(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        }
        else
        {
            DFSCommand dfsCommand=new DFSCommand( Integer.parseInt(args[0]), 0);
        }
     } 
}
