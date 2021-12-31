package il.ac.hit;

import il.ac.hit.model.CostManagerException;
import il.ac.hit.model.CostManagerModel;
import il.ac.hit.model.Model;
import il.ac.hit.view.ViewManager;
import il.ac.hit.view.View;
import il.ac.hit.viewmodel.CostManagerViewModel;
import il.ac.hit.viewmodel.ViewModel;

import javax.swing.*;

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


        Model model = new CostManagerModel();
        ViewModel viewModel = new CostManagerViewModel();
        View view = new ViewManager();

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
