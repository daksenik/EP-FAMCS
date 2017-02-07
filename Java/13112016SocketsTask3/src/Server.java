import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

/**
 * Created by daksenik on 13.11.2016.
 */
public class Server extends Thread {
    ArrayList<String>dirTree = new ArrayList<>();
    boolean treeSent = false;
    String startPath = "D:\\";

    public void run(){
        buildDirectoryTree();
        ServerSocket ss;
        try {
            ss = new ServerSocket(8000);
            Socket client;
            while(true){
                client = ss.accept();
                if(client != null) {
                    new ClientProcess(client,startPath,this).start();
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }

    void buildDirectoryTree(){
        try {
            int remSymb = startPath.length();
            Files.walkFileTree(FileSystems.getDefault().getPath(startPath, "Books"), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attr){
                    long len = new File(file.toString()).length();
                    dirTree.add(len+"\\"+file.toString().substring(remSymb));
                    return FileVisitResult.CONTINUE;
                }
            });
        }catch(Exception e){
            System.out.println(e);
        }
    }

    void sendTree(Socket client){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
            oos.writeObject(dirTree);
            treeSent = true;
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        new Server().start();
    }
}


class ClientProcess extends Thread{
    Socket client;
    String startPath;
    Server parent;
    public ClientProcess(Socket clnt,String sp,Server p){
        super();
        parent = p;
        startPath = sp;
        client = clnt;
    }

    @Override
    public void run(){
        try {
            while(client.getInputStream().available() <= 0)Thread.sleep(1);
            String path = (String)(new ObjectInputStream(client.getInputStream()).readObject());
            if(path.equals("\\GETDIRTREE\\")){
                parent.sendTree(client);
                client.close();
                return;
            }
            OutputStream oos = client.getOutputStream();
            System.out.println(startPath+path);
            FileInputStream fis = new FileInputStream(startPath+path);
            byte[]bytes = new byte[1000];
            int len;
            while(fis.available() > 0){
                len = fis.read(bytes);
                for(int i=0;i<len;i++)oos.write(bytes[i]);
                oos.flush();
            }
            oos.close();
            fis.close();
            client.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }
}