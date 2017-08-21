package com.example.vanguard.responses;

import com.example.vanguard.questions.Answer;

/**
 * Created by BertTurtle on 6/1/2017.
 */

public interface Response<T> extends Answer<T> {

	boolean isPracticeMatchResponse();

	String getEventKey();

}
