package consultation.psy.test;

import consultation.psy.entities.Consultation;
import consultation.psy.entities.Psy;
import consultation.psy.services.ServiceConsultation;
import consultation.psy.services.ServicePsy;

import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException {
        ServiceConsultation sc = new ServiceConsultation();
        ServicePsy spy = new ServicePsy();


        try {
            spy.ajouter(new Psy("abdelrahmen",22562390,"ines@gmail.com","enfant","17-08-2029","17:30"));
            System.out.println("ajouter psy!!!");
            //spy.modifier(new Psy(16,"maha",20893859,"ines@gmail.com","chdakhlek","17-08-2029","17:30"));
           // System.out.println("MODFIER psy!!!");

             System.out.println(spy.getList());


            //spy.supprimer(16);
            // System.out.println("Supprimerrr psy!!!");


        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        try {
          //  sc.ajouter(new Consultation("17-5-2544","11:30","haja themni", "en attente","tkassart"));
         // System.out.println("ajouter consultation!!!");

            //sc.modifier(new Consultation(37,"17-5-2058","17:30","tkhossekch", "en attente","chdakhlek"));
            //System.out.println("MODFIER consultation!!!");

            System.out.println(sc.getList());


           // sc.supprimer(37);
            // System.out.println("Supprimerrr consultation!!!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }





















    }
}