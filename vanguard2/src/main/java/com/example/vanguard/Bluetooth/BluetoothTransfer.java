package com.example.vanguard.Bluetooth;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import com.example.vanguard.Pages.Activities.MainActivity;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.Responses.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mbent on 7/20/2017.
 */

public class BluetoothTransfer {

	private Context context;
	private BluetoothAdapter bluetoothAdapter;
	private ProgressDialog progressDialog;


	public BluetoothTransfer(BluetoothSocket socket, Context context, BluetoothAdapter bluetoothAdapter, ProgressDialog progressDialog) {
		this.context = context;
		this.bluetoothAdapter = bluetoothAdapter;
		this.progressDialog = progressDialog;

		ConnectedThread thread = new ConnectedThread(socket);
		try {
			thread.write(BluetoothManager.serializeObject(MainActivity.databaseManager.getQuestionMaps()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		thread.start();

		System.out.println("DONE CONSTRUCTOR");

	}

	private class ConnectedThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;

		public ConnectedThread(BluetoothSocket socket) {
			mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			// Get the input and output streams; using temp objects because
			// member streams are final.
			try {
				tmpIn = socket.getInputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}

			mmInStream = tmpIn;
			mmOutStream = tmpOut;
		}

		public void run() {

			// Keep listening to the InputStream until an exception occurs.
			while (true) {
				try {
					// Read from the InputStream.
					byte[] buffer = new byte[16384];
					byte[] tempBuffer = new byte[4];
					this.mmInStream.read(tempBuffer);
					int length = new BigInteger(tempBuffer).intValue();
					int totalSize = 0;
					while (totalSize < length) {
						totalSize += this.mmInStream.read(buffer, totalSize, buffer.length - totalSize);
					}

					if (buffer.length > 0) {
						System.out.println("DONE READING");
						interpretData(buffer);
						break;
					}

				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			}
			System.out.println("DONE INTERPRETING");

			this.cancel();
			bluetoothAdapter.disable();
			progressDialog.dismiss();

		}

		// Call this from the main activity to send data to the remote device.
		public void write(byte[] bytes) {
			try {
				mmOutStream.write(ByteBuffer.allocate(4).putInt(bytes.length).array());
				mmOutStream.write(bytes);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("DONE WRITING");
		}

		// Call this method from the main activity to shut down the connection.
		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private void interpretData(byte[] bytes) {
			boolean isServer = BluetoothManager.isServer(context);

			List<Map<String, Object>> questionMaps = null;

			try {
				questionMaps = (List<Map<String, Object>>) BluetoothManager.deserializeObject(bytes);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (questionMaps != null) {
				AnswerList<Question> currentQuestions = MainActivity.databaseManager.getAllQuestions();
				List<Integer> teams = MainActivity.databaseManager.getCurrentEventTeams();
				for (Map<String, Object> questionMap : questionMaps) {
					Question transferredQuestion = MainActivity.databaseManager.getQuestionVariableFromMap(questionMap);
					Question correspondingQuestion = currentQuestions.getQuestionById(transferredQuestion.getID());
					currentQuestions.remove(correspondingQuestion);
					if (correspondingQuestion == null && !isServer) {
						// If the question is null and this is not the server.
						MainActivity.databaseManager.createQuestion(transferredQuestion);
					} else if (correspondingQuestion == null) {
						// If the question is null and this is the server.
					} else if (transferredQuestion.isMatchQuestion()) {
						// If the question is a match question.
						correspondingQuestion.addResponses(transferredQuestion.getResponses());
						MainActivity.databaseManager.saveResponses(transferredQuestion);
					} else if (!isServer) {
						// If the question is not a match question and this is not the server.
						for (int team : teams) {
							AnswerList<Response> correspondingResponses = correspondingQuestion.getTeamResponses(team, false);
							AnswerList<Response> transferredResponses = transferredQuestion.getTeamResponses(team, false);
							if (!correspondingResponses.equals(transferredResponses)) {
								correspondingQuestion.removeResponses(correspondingResponses);
								correspondingQuestion.addResponses(transferredResponses);
							}
						}
						MainActivity.databaseManager.saveResponses(correspondingQuestion);
					} else {
						// If the question is not a match question and this is the server.
						for (int team : teams) {
							AnswerList<Response> correspondingResponses = correspondingQuestion.getTeamResponses(team, false);
							AnswerList<Response> transferredResponses = transferredQuestion.getTeamResponses(team, false);
							if (correspondingResponses.size() == 0) {
								correspondingQuestion.addResponses(transferredResponses);
							}
						}
						MainActivity.databaseManager.saveResponses(correspondingQuestion);
					}
				}
				for (Question question : currentQuestions) {
					MainActivity.databaseManager.deleteQuestion(question);
				}
			}
		}
	}
}