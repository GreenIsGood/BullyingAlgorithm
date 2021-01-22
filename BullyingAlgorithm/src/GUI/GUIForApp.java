package GUI;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import processHandling.ProcessesHandler;
import sharedData.ProcessDataRemoteInterface;

import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

public class GUIForApp implements Checker {
	public JFrame frame;
	JTextField createProcField;
	JTextField deleteProcField;
	JTextField coordinatorId;
	JComboBox<Integer> processesList;
	JTextArea communicationTxtField;
	ProcessesHandler processesHandler;
	ScheduledExecutorService exec;


	public GUIForApp() {
		initialize();
	}

	void initialize() {
		processesHandler = new ProcessesHandler();

		frame = new JFrame();
		frame.setBounds(100, 100, 880, 579);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel coordinatorLabel = new JLabel("Current Coordinator Is Process ");
		coordinatorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		coordinatorLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
		coordinatorLabel.setBounds(185, 10, 340, 52);
		frame.getContentPane().add(coordinatorLabel);

		JLabel processesLabel = new JLabel("Choose Processor");
		processesLabel.setHorizontalAlignment(SwingConstants.CENTER);
		processesLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
		processesLabel.setBounds(44, 115, 195, 35);
		frame.getContentPane().add(processesLabel);

		JLabel inputCreateLabel = new JLabel("Input a number to create a process");
		inputCreateLabel.setHorizontalAlignment(SwingConstants.CENTER);
		inputCreateLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
		inputCreateLabel.setBounds(458, 115, 408, 35);
		frame.getContentPane().add(inputCreateLabel);

		processesList = new JComboBox<Integer>();
		processesList.setFont(new Font("Tahoma", Font.PLAIN, 20));
		processesList.setBounds(54, 164, 182, 35);
		frame.getContentPane().add(processesList);
		processesList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JComboBox<?> is = (JComboBox<?>) e.getSource();
					if (is.getSelectedItem() != null) {
						int id = (Integer) is.getSelectedItem();
						refreshProcessesListField(id);
						refreshCurrentCoordinatorField();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		JLabel processorComLabel = new JLabel("Processor Communication");
		processorComLabel.setHorizontalAlignment(SwingConstants.CENTER);
		processorComLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
		processorComLabel.setBounds(10, 214, 292, 35);
		frame.getContentPane().add(processorComLabel);

		JButton createButton = new JButton("Create");
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int pId = getCreateProcessIdField();
				if (pId == Integer.MIN_VALUE)
					return;
				if (pId < 0 || pId >= 1000) {
					JOptionPane.showMessageDialog(null, "Process Id is out of bounds", "InfoBox: Wrong Input",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (processesHandler.startProcess(pId) == false) {
					JOptionPane.showMessageDialog(null, "Process is already alive", "InfoBox: Wrong Input",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "Process created", "InfoBox: Done",
							JOptionPane.INFORMATION_MESSAGE);
					setProcessesListField(processesHandler.getIds());
					refreshCurrentCoordinatorField();
				}
			}
		});
		createButton.setFont(new Font("Tahoma", Font.PLAIN, 24));
		createButton.setBounds(574, 214, 209, 35);
		frame.getContentPane().add(createButton);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 271, 377, 221);
		frame.getContentPane().add(scrollPane);

		communicationTxtField = new JTextArea();
		scrollPane.setViewportView(communicationTxtField);
		communicationTxtField.setEditable(false);
		communicationTxtField.setFont(new Font("Monospaced", Font.PLAIN, 16));

		createProcField = new JTextField();
		createProcField.setFont(new Font("Tahoma", Font.PLAIN, 20));
		createProcField.setHorizontalAlignment(SwingConstants.CENTER);
		createProcField.setBounds(574, 160, 209, 44);
		frame.getContentPane().add(createProcField);
		createProcField.setColumns(10);

		JLabel inputDeleteLabel = new JLabel("Input a number to delete a process");
		inputDeleteLabel.setHorizontalAlignment(SwingConstants.CENTER);
		inputDeleteLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
		inputDeleteLabel.setBounds(448, 292, 408, 35);
		frame.getContentPane().add(inputDeleteLabel);

		deleteProcField = new JTextField();
		deleteProcField.setFont(new Font("Tahoma", Font.PLAIN, 20));
		deleteProcField.setHorizontalAlignment(SwingConstants.CENTER);
		deleteProcField.setColumns(10);
		deleteProcField.setBounds(574, 337, 209, 44);
		frame.getContentPane().add(deleteProcField);

		JButton deleteBtn = new JButton("Delete");
		deleteBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int pId = getDeleteProcessIdField();
				if (pId == Integer.MIN_VALUE)
					return;
				if (pId < 0 || pId >= 1000) {
					JOptionPane.showMessageDialog(null, "Process Id is out of bounds", "InfoBox: Wrong Input",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (processesHandler.deleteProcess(pId) == false) {
					JOptionPane.showMessageDialog(null, "Process is already dead", "InfoBox: Wrong Input",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "Process killed", "InfoBox: Done",
							JOptionPane.INFORMATION_MESSAGE);
					setProcessesListField(processesHandler.getIds());
					refreshCurrentCoordinatorField();
				}
			}

		});
		deleteBtn.setFont(new Font("Tahoma", Font.PLAIN, 24));
		deleteBtn.setBounds(574, 391, 209, 35);
		frame.getContentPane().add(deleteBtn);

		coordinatorId = new JTextField();
		coordinatorId.setEditable(false);
		coordinatorId.setFont(new Font("Tahoma", Font.PLAIN, 20));
		coordinatorId.setHorizontalAlignment(SwingConstants.CENTER);
		coordinatorId.setBounds(527, 18, 64, 44);
		frame.getContentPane().add(coordinatorId);
		coordinatorId.setColumns(10);

		updateUI();

	}

	private void updateUI() {
		exec = Executors.newSingleThreadScheduledExecutor();
		exec.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				refreshCurrentCoordinatorField();
				if (processesList.getSelectedItem() != null) {
					refreshProcessesListField(Integer.parseInt(processesList.getSelectedItem().toString()));
				}
			}
		}, 0, 3000, TimeUnit.MILLISECONDS);
	}

	void refreshProcessesListField(int id) {
		try {
			FileWriter w = new FileWriter("LogForProcessNo " + id + ".txt", true);
			ProcessDataRemoteInterface currentStub = processesHandler.getStub()[id];
			w.write(currentStub.getMsg());
			currentStub.setMsg("");
			w.close();
			FileReader r = new FileReader("LogForProcessNo " + id + ".txt");
			int c;
			String msg = "";
			while ((c = r.read()) != -1) {
				msg += (char) c;
			}
			r.close();
			setCommunicationLogField(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void refreshCurrentCoordinatorField() {
		if (processesHandler.getCurrentCoordinator() == -1) {
			coordinatorId.setText("Dead");
		} else {
			setCoordinatorIdField(processesHandler.getCurrentCoordinator());
		}
	}

	void setCoordinatorIdField(int id) {
		coordinatorId.setText(((Integer) id).toString());
	}

	void setProcessesListField(int[] id) {
		processesList.removeAllItems();
		for (int i = 0; i < id.length; i++) {
			processesList.addItem(id[i]);
		}
	}

	void setCommunicationLogField(String log) {
		communicationTxtField.setText(log);
	}

	int getCreateProcessIdField() {
		String number = createProcField.getText();
		createProcField.setText("");
		if (isNumeric(number)) {
			return Integer.parseInt(number);
		} else {
			JOptionPane.showMessageDialog(null, "You should input an integer", "InfoBox: " + "Wrong Format",
					JOptionPane.INFORMATION_MESSAGE);
			return Integer.MIN_VALUE;
		}
	}

	int getDeleteProcessIdField() {
		String number = deleteProcField.getText();
		deleteProcField.setText("");
		if (isNumeric(number)) {
			return Integer.parseInt(number);
		} else {
			JOptionPane.showMessageDialog(null, "You should input an integer", "InfoBox: " + "Wrong Format",
					JOptionPane.INFORMATION_MESSAGE);
			return Integer.MIN_VALUE;
		}
	}

}
