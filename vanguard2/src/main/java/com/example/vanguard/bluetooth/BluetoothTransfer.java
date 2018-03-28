package com.example.vanguard.bluetooth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.vanguard.pages.activities.MainActivity;
import com.example.vanguard.questions.AnswerList;
import com.example.vanguard.questions.Question;
import com.example.vanguard.responses.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

/**
 * Created by mbent on 7/20/2017.
 */

public class BluetoothTransfer {

	private Activity context;
	private BluetoothAdapter bluetoothAdapter;


	public BluetoothTransfer(BluetoothSocket socket, Activity context, BluetoothAdapter bluetoothAdapter) {
		this.context = context;
		this.bluetoothAdapter = bluetoothAdapter;

		Log.d("Sending Data", "beep beep");

		ConnectedThread thread = new ConnectedThread(socket);
		thread.start();
		try {
			thread.write(BluetoothManager.serializeObject(MainActivity.databaseManager.getQuestionMaps()));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			Looper.prepare();
			Log.d("Running", "Running");

			// Keep listening to the InputStream until an exception occurs.
			while (true) {
				Log.d("Loop", "LOOOOP");
				try {
					// Read from the InputStream.
					byte[] buffer = new byte[65536];
					byte[] tempBuffer = new byte[4];
					Log.d("Read", "1");
					this.mmInStream.read(tempBuffer);
					Log.d("Read", "2");
					int length = new BigInteger(tempBuffer).intValue();
					int totalSize = 0;
					Log.d("Length", String.valueOf(length));
					while (totalSize < length) {
						Log.d("Read", String.valueOf(totalSize));
						totalSize += this.mmInStream.read(buffer, totalSize, buffer.length - totalSize);
					}

					Log.d("Buffer Length", String.valueOf(buffer.length));
					if (buffer.length > 0) {
						interpretData(buffer);
						break;
					}

				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			}

			context.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(context, "Transfer Complete", Toast.LENGTH_LONG).show();
				}
			});

			this.cancel();
		}

		// Call this from the main activity to send data to the remote device.
		public void write(byte[] bytes) {
			Log.d("Writing", "Write");
			try {
				mmOutStream.write(ByteBuffer.allocate(4).putInt(bytes.length).array());
				mmOutStream.write(bytes);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.d("Writing", "Wrote");
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
			Log.d("Interpret", "Getting Data");
			context.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(context, "Interpreting Data", Toast.LENGTH_LONG).show();
				}
			});
			boolean isServer = BluetoothManager.isServer(context);

			List<Map<String, Object>> questionMaps = null;

			try {
				questionMaps = (List<Map<String, Object>>) BluetoothManager.deserializeObject(bytes);
			} catch (Exception e) {
				e.printStackTrace();
			}


			if (questionMaps != null) {
				Log.d("QuestionsMaps", questionMaps.toString());
				AnswerList<Question> currentQuestions = MainActivity.databaseManager.getAllQuestions();
				List<Integer> teams = MainActivity.databaseManager.getCurrentEventTeams();
				for (Map<String, Object> questionMap : questionMaps) {
					Question transferredQuestion = MainActivity.databaseManager.getQuestionVariableFromMap(questionMap);
					Question correspondingQuestion = currentQuestions.getQuestionById(transferredQuestion.getID());

					if (!isServer) {
						if (correspondingQuestion != null && transferredQuestion != null) {
							if (!correspondingQuestion.getQuestionProperties().equals(transferredQuestion.getQuestionProperties())) {
								MainActivity.databaseManager.setQuestionProperties(correspondingQuestion, transferredQuestion.getQuestionProperties());
							}
						}
					}

					Log.d("Corresponding Question", String.valueOf(correspondingQuestion));
					Log.d("Transferred Question", String.valueOf(transferredQuestion));
					currentQuestions.remove(correspondingQuestion);
					if (correspondingQuestion == null && !isServer) {
						// If the question is null and this is not the server.
						MainActivity.databaseManager.createQuestion(transferredQuestion);
					} else if (correspondingQuestion == null) {
						// If the question is null and this is the server.
					} else if (transferredQuestion.isMatchQuestion()) {
						// If the question is a match question.
						AnswerList<Response> transferredResponses = transferredQuestion.getResponses();
						for (Response response : transferredResponses) {
							if (!correspondingQuestion.getResponses().contains(response)) {
								correspondingQuestion.addResponse(response);
							}
						}
						MainActivity.databaseManager.saveResponses(correspondingQuestion);
					} else if (!isServer) {
						// If the question is not a match question and this is not the server.
						for (int team : teams) {
							AnswerList<Response> correspondingResponses = correspondingQuestion.getTeamResponses(team, false);
							AnswerList<Response> transferredResponses = transferredQuestion.getTeamResponses(team, false);
							if (!correspondingResponses.equals(transferredResponses)) {
								if (transferredResponses.size() > 0) {
									correspondingQuestion.removeResponses(correspondingResponses);
									correspondingQuestion.addResponses(transferredResponses);
								}
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
				if (!isServer) {
					for (Question question : currentQuestions) {
						MainActivity.databaseManager.deleteQuestion(question);
					}
				}

			}
		}
	}
}