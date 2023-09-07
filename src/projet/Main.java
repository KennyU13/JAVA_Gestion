/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package projet;
import com.gestion.model.Conversion;
import com.gestion.view.Window;

/**
 *
 * @author routs
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        Window ui = new Window(); 
//        ui.setVisible(true);
//        ui.pack();
//        ui.setLocationRelativeTo(null);

        String x = Conversion.convertir(10800);
        System.out.println(x);
        
    }
    
}
