package com.example.android.tflitecamerademo;



/*
 * Copyright 2016 The gRPC Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import net.razorvine.pickle.Pickler;
import net.razorvine.pickle.objects.ByteArrayConstructor;

import android.app.Activity;
import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import com.example.android.tflitecamerademo.ImageClassGrpc.ImageClassBlockingStub;
import com.example.android.tflitecamerademo.ImageClassGrpc.ImageClassStub;


import com.google.protobuf.ByteString;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.text.MessageFormat;

public class GrpcActivity extends Activity {
    private EditText hostEdit;
    private EditText portEdit;
    private Button startRouteGuideButton;
    private Button exitRouteGuideButton;
    private Button getFeatureButton;
    private ImageView imageView;

    private TextView resultText;
    private ManagedChannel channel;
    private Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routeguide);
        hostEdit = (EditText) findViewById(R.id.host_edit_text);
        portEdit = (EditText) findViewById(R.id.port_edit_text);
        startRouteGuideButton = (Button) findViewById(R.id.start_route_guide_button);
        exitRouteGuideButton = (Button) findViewById(R.id.exit_route_guide_button);
        getFeatureButton = (Button) findViewById(R.id.get_feature_button);
        resultText = (TextView) findViewById(R.id.result_text);
        resultText.setMovementMethod(new ScrollingMovementMethod());
        disableButtons();
    }

    public void startRouteGuide(View view) {
        String host = hostEdit.getText().toString();
        String portStr = portEdit.getText().toString();
        int port = TextUtils.isEmpty(portStr) ? 0 : Integer.valueOf(portStr);
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(hostEdit.getWindowToken(), 0);



        // her eis where the channel is opened
        channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        hostEdit.setEnabled(false);
        portEdit.setEnabled(false);
        startRouteGuideButton.setEnabled(false);
        enableButtons();
    }




    public void exitRouteGuide(View view) {
        channel.shutdown();
        disableButtons();
        hostEdit.setEnabled(true);
        portEdit.setEnabled(true);
        startRouteGuideButton.setEnabled(true);
    }

    public void getFeature(View view) {
        setResultText("");
        disableButtons();
        Log.i("connect", "getFeature");


        new GrpcTask(new GetClassRunnable(), channel, this).execute();
    }



    private void setResultText(String text) {
        resultText.setText(text);
    }

    private void disableButtons() {
        getFeatureButton.setEnabled(false);

        exitRouteGuideButton.setEnabled(false);
    }

    private void enableButtons() {
        exitRouteGuideButton.setEnabled(true);
        getFeatureButton.setEnabled(true);

    }








    private static class GrpcTask extends AsyncTask<Void, Void, String> {
        private final GrpcRunnable grpcRunnable;
        private final ManagedChannel channel;
        private final WeakReference<GrpcActivity> activityReference;

        GrpcTask(GrpcRunnable grpcRunnable, ManagedChannel channel, GrpcActivity activity) {
            this.grpcRunnable = grpcRunnable;
            this.channel = channel;

            this.activityReference = new WeakReference<GrpcActivity>(activity);
        }

        @Override
        protected String doInBackground(Void... nothing) {
            try {
                String logs =
                        grpcRunnable.run(
                                ImageClassGrpc.newBlockingStub(channel), ImageClassGrpc.newStub(channel));
                return "Success!!!\n" + logs;
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                pw.flush();
                return "Failed... :\n" + sw;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            GrpcActivity activity = activityReference.get();
            if (activity == null) {
                Log.i("connect","activity");
                return;
            }
            activity.setResultText(result);
            activity.enableButtons();
        }
    }

    private interface GrpcRunnable {
        /** Perform a grpcRunnable and return all the logs. */
        String run(ImageClassBlockingStub blockingStub, ImageClassStub asyncStub) throws Exception;
    }

    private  class GetClassRunnable implements GrpcRunnable {
        @Override
        public String run(ImageClassBlockingStub blockingStub, ImageClassStub asyncStub)
                throws Exception {
            String ret  ;
            ret =getFeature(blockingStub);
            if (ret==null){
                Log.i("connect","null return");

            }
            return ret;

        }



        /** Blocking unary call example. Calls getFeature and prints the response. */


        private String getFeature( ImageClassBlockingStub blockingStub)
                throws StatusRuntimeException, IOException {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.stage1);

            if(bitmap==null){

                Log.i("connect", "bitmap null ");

            }


            Log.i("connect", "connection is working11111 ");

            StringBuffer logs = new StringBuffer();
            appendLogs(logs, "*** GetFeature: ");

            Log.i("connect", "connection is append ");
            Pickler pickler = new Pickler();
            ByteString data = ByteString.copyFrom(pickler.dumps(getBytesFromBitmap(bitmap)));
            // here is were i send the image to grpc
            GrpcRequest request = GrpcRequest.newBuilder().setImage(ByteString.copyFrom(getBytesFromBitmap(bitmap))).build();

            GrpcReply feature;
            if (request != null) {
                Log.i("connect", "start request");

                feature = blockingStub.getStage(request);
                Log.i("connect", "request finished  ");


                if (true) {
                    Log.i("connect", "connection is working");

                    appendLogs(
                            logs,
                            "Found feature called \"{0}\" at {1}, {2}",
                            feature.getStage());
                } else {
                    Log.i("connect", "connection is working2");

                    appendLogs(
                            logs,
                            "Found no feature at {0}, {1}", feature.getStageAcc());
                }







            }
            return logs.toString();}
        // convert from bitmap to byte array
        public byte[] getBytesFromBitmap(Bitmap bitmap) {

            if(bitmap==null){

                Log.i("connect", "bitmap null ");

            }
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 70, stream);
            return stream.toByteArray();
        }
    }





    private static void appendLogs(StringBuffer logs, String msg, Object... params) {
        if (params.length > 0) {
            logs.append(MessageFormat.format(msg, params));
        } else {
            logs.append(msg);
        }
        logs.append("\n");
    }





}
