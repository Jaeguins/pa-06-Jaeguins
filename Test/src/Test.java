import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Test {
    Random random = new Random();
    String[] keys = new String[2];
    String buffer;
    HashTable table;
    int MaximumWord;
    public static void main(String args[]) {
        Scanner scan=new Scanner(System.in);
        System.out.println("input :\n1. size of hash table\n2. hash multiplier\n3. maximum length-of output(number of words)\n4. first word of output\n5. second word of output");
        Test t = new Test(scan.nextInt(),scan.nextInt(),scan.nextInt());
        try {
            t.InputData("HarryPotter.txt");
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        System.out.println("Input finished");
        String out=t.GenerateData(scan.next(),scan.next());
        System.out.println(out);
    }

    public Test(int size,int mult,int max){
        MaximumWord=max;
        table=new HashTable(size,mult);
    }

    public void InputData(String path) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(path));
        keys[0] = scanner.next();
        keys[1] = scanner.next();
        int crashCount=0;
        while (scanner.hasNext()) {
            buffer = scanner.next();

            int pref=table.hash(keys[0],keys[1]);
            if(table.table[pref]==null)
                table.table[pref]=new Prefix(keys[0],keys[1]);
            if(!table.table[pref].pword1.equals(keys[0])||!table.table[pref].pword2.equals(keys[1]))
                crashCount++;
            Prefix prefix=table.table[pref];
            if(prefix.suffix==null){
                prefix.suffix=new Suffix(buffer);
            }
            Suffix suff=prefix.suffix;
            while(true)
            if(suff.sword.equals(buffer)){
                suff.count++;
                prefix.suffixCount++;
                break;
            }else{
                if(suff.next==null){
                    suff.next=new Suffix(buffer);
                    break;
                }else{
                    suff=suff.next;
                }
            }
            keys[0] = keys[1];
            keys[1] = buffer;
        }
        System.out.println("hashing crahsed : "+crashCount);
    }

    public String GenerateData(String first, String second) {
        int count=2;
        keys[0] = first;
        keys[1] = second;
        String ret = first+' '+second;
        System.out.print(ret);
        while(count++<MaximumWord){
            int t=table.hash(keys[0],keys[1]);
            Prefix pref=table.table[t];
            if(pref==null||pref.suffixCount==0)break;
            int index=random.nextInt(pref.suffixCount);
            Suffix suff=pref.suffix;
            while(index>0){
                index-=suff.count;
                if(suff.next==null)break;
                suff=suff.next;
            }
            System.out.print(' '+suff.sword);
            ret+=' '+suff.sword;

            keys[0]=keys[1];
            keys[1]=suff.sword;
        }
        return ret;
    }

}
class Suffix{
    String sword;

    public Suffix(String sword) {
        this.sword = sword;
    }
    @Override
    public String toString(){
        return sword;
    }
    int count=1;
    Suffix next;
}
class Prefix{
    String pword1;
    String pword2;
    Suffix suffix;
    int suffixCount=0;

    public Prefix(String pword1, String pword2) {
        this.pword1 = pword1;
        this.pword2 = pword2;
    }
}
class HashTable{
    int HashSize,Multiplier;
    Prefix[] table;
    public HashTable(int size,int Multiplier){
        this.Multiplier=Multiplier;
        HashSize=size;
        table=new Prefix[size];
    }
    int hash(String key1,String key2){
        int h=0;
        for(char t:key1.toCharArray())
            h+=h*Multiplier+t;
        for(char t:key2.toCharArray())
            h+=h*Multiplier+t;
        h&=0b01111111_11111111_11111111_11111111;
        return h%HashSize;
    }
}