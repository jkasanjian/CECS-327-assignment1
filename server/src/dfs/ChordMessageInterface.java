package dfs;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.MusicClass;

public interface ChordMessageInterface extends Remote, Serializable
{
    public ChordMessageInterface getPredecessor()  throws RemoteException;
    ChordMessageInterface locateSuccessor(long key) throws RemoteException;
    ChordMessageInterface closestPrecedingNode(long key) throws RemoteException;
    public void joinRing(String Ip, int port)  throws RemoteException;
    public void joinRing(ChordMessageInterface successor)  throws RemoteException;
    public void notify(ChordMessageInterface j) throws RemoteException;
    public boolean isAlive() throws RemoteException;
    public long getId() throws RemoteException;
    public List<MusicClass> search( String file, String queryString ) throws RemoteException, FileNotFoundException;
    
    
    public void put(long guidObject, RemoteInputFileStream inputStream) throws IOException, RemoteException;
    public void put(long guidObject, String text) throws IOException, RemoteException;
    public RemoteInputFileStream get(long guidObject) throws IOException, RemoteException;   
    public byte[] get(long guidObject, long offset, int len) throws IOException, RemoteException;  
    public void delete(long guidObject) throws IOException, RemoteException;
}
