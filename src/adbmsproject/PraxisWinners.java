/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adbmsproject;

import java.awt.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import static javax.swing.WindowConstants.HIDE_ON_CLOSE;

public class PraxisWinners extends JFrame
{
    public PraxisWinners()
    {
        Vector columnNames = new Vector();
        Vector data = new Vector();

        try
        {
            //  Connect to an Access Database

            String driver = "sun.jdbc.odbc.JdbcOdbcDriver";
//            String url = "jdbc:odbc:???";  // if using ODBC Data Source name
            String url =
                "jdbc:odbc:Festival";
            String userid = "";
            String password = "";

            Class.forName( driver );
            Connection connection = DriverManager.getConnection( url, userid, password );

            //  Read data from a table

            String sql = "SELECT (SELECT Name FROM Student WHERE ([win].Student_id = Student_id)) AS [Student Name],[Event_Name],[Rank] FROM [win] WHERE [Fest_Name]='Praxis'";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery( sql );
            ResultSetMetaData md = rs.getMetaData();
            int columns = md.getColumnCount();

            //  Get column names

            for (int i = 1; i <= columns; i++)
            {
                columnNames.addElement( md.getColumnName(i) );
            }

            //  Get row data

            while (rs.next())
            {
                Vector row = new Vector(columns);

                for (int i = 1; i <= columns; i++)
                {
                    row.addElement( rs.getObject(i) );
                }

                data.addElement( row );
            }

            rs.close();
            stmt.close();
            connection.close();
        }
        catch(Exception e)
        {
            System.out.println( e );
        }

        //  Create table with database data

        JTable table = new JTable(data, columnNames)
        {
            public Class getColumnClass(int column)
            {
                for (int row = 0; row < getRowCount(); row++)
                {
                    Object o = getValueAt(row, column);

                    if (o != null)
                    {
                        return o.getClass();
                    }
                }

                return Object.class;
            }
        };

        JScrollPane scrollPane = new JScrollPane( table );
        getContentPane().add( scrollPane );

        JPanel buttonPanel = new JPanel();
        getContentPane().add( buttonPanel, BorderLayout.SOUTH );
    }

    public static void main(String[] args)
    {
        PraxisWinners frame = new PraxisWinners();
        frame.setDefaultCloseOperation( HIDE_ON_CLOSE );
        frame.pack();
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}
