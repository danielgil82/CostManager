package il.ac.hit;

import il.ac.hit.exceptions.CostManagerException;
import il.ac.hit.model.CostManagerModel;
import il.ac.hit.model.IModel;
import il.ac.hit.view.CostManagerView;
import il.ac.hit.view.IView;
import il.ac.hit.viewModel.CostManagerViewModel;
import il.ac.hit.viewModel.IViewModel;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main
{
    private static final String dataBaseUserName = "sigalit";
    private static final String dataBasePassword= "leybman";
    private static final String driverFullQualifiedName = "com.mysql.jdbc.Driver";
    private static final String connectionStringToDB = "jdbc:mysql://localhost:3306/costmanagerproj";

    public static void main(String args[]) throws CostManagerException
    {
//       try
//        {
//            Class.forName(driverFullQualifiedName);
//            Connection connection = DriverManager.getConnection(connectionStringToDB, dataBaseUserName, dataBasePassword);
//            Statement statement = connection.createStatement();
//
//
////            ResultSet rs = statement.executeQuery("select * from categorieslist");
////            while(rs.next()){
////                System.out.println("ID:" + rs.getInt(1) + "Category Name: " + rs.getString(2));
////            }
//        }
//        catch (Exception e)
//        {
//            System.out.println("Unable to connect to the server");
//        }


        IModel model = new CostManagerModel();
        IViewModel viewModel = new CostManagerViewModel();
        IView view = new CostManagerView();

        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                view.init();
                view.start();
            }
        });
        viewModel.setModel(model);
        viewModel.setView(view);
        view.setIViewModel(viewModel);
    }
}
