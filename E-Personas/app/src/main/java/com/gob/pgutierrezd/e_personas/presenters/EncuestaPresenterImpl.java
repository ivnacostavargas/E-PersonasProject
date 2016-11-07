package com.gob.pgutierrezd.e_personas.presenters;

import android.content.Context;

import com.gob.pgutierrezd.e_personas.interactors.EncuestaInteractorImpl;
import com.gob.pgutierrezd.e_personas.interfaces.encuesta.EncuestaInteractor;
import com.gob.pgutierrezd.e_personas.interfaces.encuesta.EncuestaPresenter;
import com.gob.pgutierrezd.e_personas.interfaces.encuesta.EncuestaView;
import com.gob.pgutierrezd.e_personas.models.AnswersInterview;
import com.gob.pgutierrezd.e_personas.models.CoordsInterview;
import com.gob.pgutierrezd.e_personas.models.InformationComplement;

/**
 * Created by pgutierrezd on 18/10/2016.
 */
public class EncuestaPresenterImpl implements EncuestaPresenter, EncuestaInteractor.OnInterviewFinishedListener{

    private EncuestaView mEncuestaView;
    private EncuestaInteractor mEncuestaInteractor;
    private Context context;

    public EncuestaPresenterImpl(EncuestaView encuestaView, Context context){
        this.mEncuestaView = encuestaView;
        this.mEncuestaInteractor = new EncuestaInteractorImpl();
        this.context = context;
    }

    @Override
    public void validateInterview(AnswersInterview answersInterview,InformationComplement informationComplement, boolean bandera) {
        if(mEncuestaView != null){
            mEncuestaView.showProgress();
            mEncuestaInteractor.sendInterview(answersInterview,informationComplement, bandera, this.context, this);
        }
    }

    @Override
    public void navigateToHome() {
        if(mEncuestaView != null){
            mEncuestaView.hideProgress();
            mEncuestaView.finishInterview();
        }
    }

    @Override
    public void errorConnectionServer() {
        if(mEncuestaView != null){
            mEncuestaView.hideProgress();
        }
    }

    @Override
    public void errorSendInterview() {
        if(mEncuestaView != null){
            mEncuestaView.setFieldErrorEmptyInterview();
            mEncuestaView.hideProgress();
        }
    }
}
