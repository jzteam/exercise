package cn.jzteam.base.io;

import java.io.IOException;
import java.nio.channels.AsynchronousChannel;

public class AioTest {
    
    public static void main(String[] args) {
        
        AsynchronousChannel channel = new AsynchronousChannel() {
            
            @Override
            public boolean isOpen() {
                // TODO Auto-generated method stub
                return false;
            }
            
            @Override
            public void close() throws IOException {
                // TODO Auto-generated method stub
                
            }
        };
        
    }

}
