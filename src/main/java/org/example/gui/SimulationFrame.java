package org.example.gui;

import org.example.model.SelectionPolicy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SimulationFrame extends JFrame {
       private JPanel p;
       private JPanel p1;
       private JPanel p2;
       private JPanel p3;
       private JRadioButton sqs;
       private JRadioButton sts;
       private JSpinner timeLimit;
       private JSpinner maxTime;
       private JSpinner minTime;
       private JSpinner maxS;
       private JSpinner minS;
       private JSpinner nrQ;
       private JSpinner nrC;
       private JButton start;
       private JButton restart;
       private java.util.List<JPanel> queues;


       public SimulationFrame(){
              this.setTitle("SETUP");
              this.setDefaultCloseOperation(EXIT_ON_CLOSE);
              this.setSize(600,200);
              this.p=new JPanel();

              this.sqs=new JRadioButton("ShortestQueueStrategy",true);
              this.sts=new JRadioButton("ShortestTimeStrategy");
              this.restart=new JButton("RESTART SIMULATION");
              ButtonGroup bg= new ButtonGroup();
              bg.add(this.sqs);
              bg.add(this.sts);
              SpinnerNumberModel m1=new SpinnerNumberModel(1,1,null,1);
              this.timeLimit=new JSpinner(m1);
              SpinnerNumberModel m2=new SpinnerNumberModel(1,0,null,1);
              this.maxTime=new JSpinner(m2);
              SpinnerNumberModel m3=new SpinnerNumberModel(1,0,null,1);
              this.minTime=new JSpinner(m3);
              SpinnerNumberModel m4=new SpinnerNumberModel(1,1,null,1);
              this.nrQ=new JSpinner(m4);
              SpinnerNumberModel m5=new SpinnerNumberModel(1,1,null,1);
              this.nrC=new JSpinner(m5);
              SpinnerNumberModel m6=new SpinnerNumberModel(1,0,null,1);
              this.minS=new JSpinner(m6);
              SpinnerNumberModel m7=new SpinnerNumberModel(1,0,null,1);
              this.maxS=new JSpinner(m7);
              this.start=new JButton("START");

              p.setLayout(new FlowLayout());

              p.add(new JLabel("Choose the strategy:"));
              p.add(sqs);
              p.add(sts);
              p.add(new JLabel("timeLimit:"));
              p.add(timeLimit);
              p.add(new JLabel("maxArrivalTime:"));
              p.add(maxTime);
              p.add(new JLabel("minArrivalTime:"));
              p.add(minTime);
              p.add(new JLabel("maxServiceTime:"));
              p.add(maxS);
              p.add(new JLabel("minServiceTime:"));
              p.add(minS);
              p.add(new JLabel("nrOfQueues:"));
              p.add(nrQ);
              p.add(new JLabel("nrOfClients:"));
              p.add(nrC);
              p.add(this.start);

              this.add(p,BorderLayout.CENTER);

              this.setVisible(true);
       }

    public SelectionPolicy getSelectedStrategy()
    {
        if(this.sqs.isSelected()) return SelectionPolicy.SHORTEST_QUEUE;
        else return SelectionPolicy.SHORTEST_TIME;
    }
    public int getTimeLimit() {
        return (int)timeLimit.getValue();
    }
    public void setTimeLimit(JSpinner timeLimit) {
        this.timeLimit = timeLimit;
    }
    public int getMaxTime() {
        return (int)maxTime.getValue();
    }
    public void setMaxTime(JSpinner maxTime) {
        this.maxTime = maxTime;
    }
    public int getMinS() {
        return (int)minS.getValue();
    }
    public int getMaxS() {
        return (int)maxS.getValue();
    }
    public int getMinTime() {
        return (int)minTime.getValue();
    }
    public void setMinTime(JSpinner minTime) {
        this.minTime = minTime;
    }
    public int getNrQ() {
        return (int)nrQ.getValue();
    }
    public void setNrQ(JSpinner nrQ) {
        this.nrQ = nrQ;
    }
    public int getNrC() {
        return (int)nrC.getValue();
    }
    public void setNrC(JSpinner nrC) {
        this.nrC = nrC;
    }
    public void addStartActionListener(ActionListener e){
           this.start.addActionListener(e);
       }
    public void addRestartActionListener(ActionListener e){this.restart.addActionListener(e);}
    public void addChangeStrategyActionListener(ActionListener e){
           this.sqs.addActionListener(e);
           this.sts.addActionListener(e);
    }

    public void startAnimation() {
              this.getContentPane().removeAll();
              this.setLayout(new BorderLayout());
              this.setSize(800,600);

              this.setTitle("WELCOME TO LIDL!");
              this.p1=new JPanel();
              this.p1.add(new JLabel("Change the strategy:"));
              this.p1.add(this.sqs);
              this.p1.add(this.sts);
              this.p1.add(this.restart);
              this.add(p1,BorderLayout.SOUTH);
              this.queues=new java.util.ArrayList<>();
              int nr=(int)this.nrQ.getValue();

              this.p2=new JPanel();
              p2.setLayout(new BoxLayout(this.p2,BoxLayout.Y_AXIS));
              this.createWaitingLines(nr);

              JScrollPane sc=new JScrollPane(p2);
              this.add(sc,BorderLayout.CENTER);

              this.p3=new JPanel();
              this.p3.setLayout(new BoxLayout(this.p3,BoxLayout.Y_AXIS));
              this.p3.setBackground(Color.cyan);
              this.paintStore((int)this.nrC.getValue());

              JScrollPane sc2=new JScrollPane(p3);
              this.add(sc2,BorderLayout.EAST);

              this.revalidate();
              this.repaint();

       }

       public void createWaitingLines(int nr) {
              ImageIcon i1=new ImageIcon("src/main/resources/cashier.png");
              Image i2=i1.getImage();
              Image i3=i2.getScaledInstance(80,80, Image.SCALE_SMOOTH);
              ImageIcon cashier=new ImageIcon(i3);

              for(int i=0;i<nr;i++)
              {
                  JPanel aux=new JPanel(new FlowLayout(FlowLayout.LEFT));
                  aux.setBorder(BorderFactory.createTitledBorder("Casa "+(i+1)));
                  aux.setBackground(Color.pink);
                  aux.add(new JLabel(cashier));
                  aux.setPreferredSize(new Dimension(800,130));

                  JScrollPane s=new JScrollPane(aux);
                  this.p2.add(s);
                  this.queues.add(aux);

              }
       }

       public void paintStore(int nrClients) {
           for(int i=0;i<nrClients;i++)
           {
               ImageIcon i1;
               int aux=(int)(Math.random()*10);
               if(aux%2==0) i1=new ImageIcon("src/main/resources/bubu.png");
               else i1=new ImageIcon("src/main/resources/dudu.png");
               Image i2=i1.getImage();
               Image i3=i2.getScaledInstance(150,80, Image.SCALE_SMOOTH);
               ImageIcon client=new ImageIcon(i3);
               JLabel l=new JLabel(client);
               l.setText("Client "+(i+1));
               this.p3.add(l);
           }

       }

       public void eraseClient(int index, int size) {
           SwingUtilities.invokeLater(()->{
               JPanel aux=this.queues.get(index);
               while((aux.getComponentCount()-1)>size)
               {
                   aux.remove(1);

               }
               aux.revalidate();
               aux.repaint();
           });
       }

    public void eraseClientFromStore(int id, int index) {
        SwingUtilities.invokeLater(()->{
            for(Component c: this.p3.getComponents())
            {
                if(c instanceof JLabel)
                {
                    JLabel l=(JLabel) c;
                     if(l.getText().equals("Client "+id))
                     {
                         this.queues.get(index).add(l);
                         this.p3.remove(c);
                         break;
                     }
                }
            }
            this.queues.get(index).revalidate();
            this.queues.get(index).repaint();
            this.p3.revalidate();
            this.p3.repaint();
        });
    }

    public void displayError(String msg) {
        SwingUtilities.invokeLater(()->{
            JOptionPane.showMessageDialog(this,msg,"EROARE!",JOptionPane.WARNING_MESSAGE);
        });
    }

    public void displayOutput(int peek, double avgWT, double avgST) {
        SwingUtilities.invokeLater(()->{
            JOptionPane.showMessageDialog(this,"PeekTime=" + peek+",\n AverageWaitingTime="+avgWT+",\n AverageServiceTime="+avgST,
                    "STATISTICS!",JOptionPane.INFORMATION_MESSAGE);
        });
    }

    public void restartToSetup()
    {
        SwingUtilities.invokeLater(()->
        {
           this.getContentPane().removeAll();
            this.setTitle("SETUP");
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            this.setSize(600,200);
            this.p.removeAll();
            ButtonGroup bg= new ButtonGroup();
            bg.add(this.sqs);
            bg.add(this.sts);
            p.setLayout(new FlowLayout());
            p.add(new JLabel("Choose the strategy:"));
            p.add(sqs);
            p.add(sts);
            p.add(new JLabel("timeLimit:"));
            p.add(timeLimit);
            p.add(new JLabel("maxArrivalTime:"));
            p.add(maxTime);
            p.add(new JLabel("minArrivalTime:"));
            p.add(minTime);
            p.add(new JLabel("maxServiceTime:"));
            p.add(maxS);
            p.add(new JLabel("minServiceTime:"));
            p.add(minS);
            p.add(new JLabel("nrOfQueues:"));
            p.add(nrQ);
            p.add(new JLabel("nrOfClients:"));
            p.add(nrC);
            p.add(this.start);
            this.add(p,BorderLayout.CENTER);
            this.revalidate();
            this.repaint();
        });
    }


}
