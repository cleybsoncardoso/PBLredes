/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buffer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;

public class Main {

    public static void main(String[] args) throws IOException {

        CharBuffer buffer = CharBuffer.allocate(48);

        buffer.put('a');
//        while (bytesRead != -1) {
//
        buffer.flip(); // coloca o buffer em modo leitura

        //buffer.clear();// coloca o buffer novamente em modo de escrita
        System.out.println(buffer.get());

        buffer.clear();// coloca o buffer novamente em modo de escrita

        buffer.put('c');

        buffer.flip();
        System.out.println(buffer.get());
        

//        }
    }

}
