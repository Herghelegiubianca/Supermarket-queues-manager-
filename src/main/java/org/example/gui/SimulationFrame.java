package org.example.gui;

import org.example.businessLogic.SelectionPolicy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SimulationFrame extends JFrame {
       private JPanel p;
       private JPanel p1;
       private JPanel p2;
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
       private JButton close;
       private java.util.List<JPanel> queues;


       public SimulationFrame(){
              this.setTitle("SETUP");
              this.setDefaultCloseOperation(EXIT_ON_CLOSE);
              this.setSize(600,200);
              this.p=new JPanel();

              this.sqs=new JRadioButton("ShortestQueueStrategy",true);
              this.sts=new JRadioButton("ShortestTimeStrategy");
              ButtonGroup bg= new ButtonGroup();
              bg.add(this.sqs);
              bg.add(this.sts);
              SpinnerNumberModel m1=new SpinnerNumberModel(1,1,null,1);
              this.timeLimit=new JSpinner(m1);
              SpinnerNumberModel m2=new SpinnerNumberModel(1,1,null,1);
              this.maxTime=new JSpinner(m2);
              SpinnerNumberModel m3=new SpinnerNumberModel(1,1,null,1);
              this.minTime=new JSpinner(m3);
              SpinnerNumberModel m4=new SpinnerNumberModel(1,1,null,1);
              this.nrQ=new JSpinner(m4);
              SpinnerNumberModel m5=new SpinnerNumberModel(1,1,null,1);
              this.nrC=new JSpinner(m5);
              SpinnerNumberModel m6=new SpinnerNumberModel(1,1,null,1);
              this.minS=new JSpinner(m6);
              SpinnerNumberModel m7=new SpinnerNumberModel(1,1,null,1);
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

       public void startAnimation()
       {
              this.getContentPane().removeAll();
              this.setLayout(new BorderLayout());
              this.setSize(800,600);

              this.setTitle("WELCOME TO LIDL!");
              this.p1=new JPanel();
              this.p1.add(new JLabel("Change the strategy:"));
              this.p1.add(this.sqs);
              this.p1.add(this.sts);
              this.close=new JButton("END");
              this.p1.add(this.close);
              this.add(p1,BorderLayout.SOUTH);
              this.queues=new java.util.ArrayList<>();
              int nr=(int)this.nrQ.getValue();

              this.p2=new JPanel();
              p2.setLayout(new BoxLayout(this.p2,BoxLayout.Y_AXIS));
              this.createWaitingLines(nr);

              JScrollPane sc=new JScrollPane(p2);
              this.add(sc,BorderLayout.CENTER);

              this.revalidate();
              this.repaint();

       }

       public void createWaitingLines(int nr)
       {

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

       public void addClient(int id, int index)
       {
              int aux=(int)(Math.random()*10);
              ImageIcon i1;

              if(aux%2==0) i1=new ImageIcon("src/main/resources/bubu.png");
              else i1=new ImageIcon("src/main/resources/dudu.png");

           Image i2=i1.getImage();
           Image i3=i2.getScaledInstance(150,80, Image.SCALE_SMOOTH);
           ImageIcon client=new ImageIcon(i3);

           JLabel l=new JLabel(client);
           l.setText("Client "+id);

           this.queues.get(index).add(l);
           this.queues.get(index).revalidate();
           this.queues.get(index).repaint();
       }

       public void eraseClient(int index, int size)
       {
           JPanel aux=this.queues.get(index);
           while((aux.getComponentCount()-1)>size)
           {
               aux.remove(1);

           }
           aux.revalidate();
           aux.repaint();
       }

    public void displayError(String msg)
    {
        JOptionPane.showMessageDialog(this,msg,"EROARE!",JOptionPane.WARNING_MESSAGE);
    }

}
