package com.example.zhivototekrug;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class myAdapterVolonterSiteBaranja extends RecyclerView.Adapter<myAdapterVolonterSiteBaranja.ViewHolder>{

    private List<Baranje> myList;
    private int rowLayout;
    public Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtAktivnost, txtOpisAktivnost, txtVreme, txtAdresa, txtImePrezime,
                txtSend, txtDone, txtKontakt, txtInfo;
        public ImageView Pic;

        public ViewHolder(View itemView) {
            super(itemView);
            txtAktivnost = (TextView) itemView.findViewById(R.id.rw1Activity);
            txtOpisAktivnost = (TextView) itemView.findViewById(R.id.rw1ActivityDesc);
            txtVreme = (TextView) itemView.findViewById(R.id.rw1DateTime);
            txtAdresa = (TextView) itemView.findViewById(R.id.rw1Location);
            txtImePrezime = (TextView) itemView.findViewById(R.id.rw1Name);
            txtSend = (TextView) itemView.findViewById(R.id.rw1Send);
            txtDone = (TextView) itemView.findViewById(R.id.rw1Done);
            txtKontakt = (TextView) itemView.findViewById(R.id.rw1Contact);
            txtInfo = (TextView) itemView.findViewById(R.id.rw1Info);
            Pic = (ImageView) itemView.findViewById(R.id.rw1Picture);

            txtDone.setVisibility(View.INVISIBLE);
            txtInfo.setVisibility(View.INVISIBLE);
            txtSend.setVisibility(View.INVISIBLE);
        }
    }

    public myAdapterVolonterSiteBaranja(List<Baranje> myList, int rowLayout, Context context) {
        this.myList = myList;
        this.rowLayout = rowLayout;
        this.mContext = context;
    }

    @Override
    public myAdapterVolonterSiteBaranja.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new myAdapterVolonterSiteBaranja.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(myAdapterVolonterSiteBaranja.ViewHolder viewHolder, int i) {
        Baranje baranje = myList.get(i);
        viewHolder.txtAktivnost.setText(baranje.getAktivnost());
        viewHolder.txtOpisAktivnost.setText("Description: "+baranje.getOpisAktivnost());
        if(baranje.getDatum().equals("")) {
            viewHolder.txtVreme.setText("Time: " + baranje.getVremeOd() + " - " + baranje.getVremeDo() + "       Every " + baranje.getDen());
        } else {
            viewHolder.txtVreme.setText("Time: " + baranje.getVremeOd() + " - " + baranje.getVremeDo() + "       Date: " + baranje.getDatum());
        }
        viewHolder.txtAdresa.setText("Address: " + baranje.getAdresa() + "       Distance: " + baranje.getRastojanie() + " km");
        viewHolder.txtAdresa.setPaintFlags(viewHolder.txtAdresa.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        if(baranje.getStatus().equals("Reserved")) {
            viewHolder.txtDone.setVisibility(View.VISIBLE);
        } else {
            viewHolder.txtDone.setVisibility(View.INVISIBLE);
        }

        if(baranje.getStatus().equals("Finished")) {
            viewHolder.txtInfo.setVisibility(View.VISIBLE);
        } else {
            viewHolder.txtInfo.setVisibility(View.INVISIBLE);
        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(baranje.getUserId());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                DecimalFormat decimalFormat = new DecimalFormat("##.0");
                if(user.getVkupnoOceni() != 0) {
                    double ocena = (double) user.getZbirOceni() / user.getVkupnoOceni();
                    viewHolder.txtImePrezime.setText(user.getName() + "    Rating: " + decimalFormat.format(ocena));
                } else {
                    viewHolder.txtImePrezime.setText(user.getName() + "    Rating: /");
                }
                if(baranje.getStatus().equals("Waiting")) {
                    viewHolder.txtKontakt.setText("");
                } else {
                    viewHolder.txtKontakt.setText("Contact: " + user.getEmail() + "      " + user.getPhone());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mContext, "An error has occurred!", Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.txtAdresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShowGoogleMapActivity.class);
                intent.putExtra("lat", baranje.getLatitude());
                intent.putExtra("lon", baranje.getLongitude());
                v.getContext().startActivity(intent);
            }
        });

        viewHolder.txtDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);

                alert.setTitle("Finished activity");

                LinearLayout layout = new LinearLayout(mContext);
                layout.setOrientation(LinearLayout.VERTICAL);

                final TextView textView = new TextView(mContext);
                textView.setText("User rating:");
                textView.setPadding(0,10,0,0);
                textView.setTextSize(16);

                final EditText izvestaj = new EditText(mContext);
                izvestaj.setHint("Submit a review");
                layout.addView(izvestaj);
                layout.addView(textView);

                final RadioGroup radioGroup = new RadioGroup(mContext);
                RadioButton radioButton;
                for(int i = 1; i < 6; i++) {
                    radioButton = new RadioButton(mContext);
                    radioButton.setText(String.valueOf(i));
                    radioGroup.addView(radioButton);
                }

                RadioButton radioButton1 = (RadioButton) radioGroup.getChildAt(4);
                radioButton1.setChecked(true);

                radioGroup.setOrientation(LinearLayout.HORIZONTAL);

                layout.addView(radioGroup);

                alert.setView(layout);

                alert.setPositiveButton(Html.fromHtml("<font color='#FFFFFF'>Submit</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        RadioButton radioButton1 = (RadioButton) layout.findViewById(radioGroup.getCheckedRadioButtonId());
                        int Ocena = Integer.parseInt(radioButton1.getText().toString());

                        Map<String, Object> map = new HashMap();

                        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("Activities");
                        map.put(baranje.getAktivnostId()+"/status", "Finished");
                        map.put(baranje.getAktivnostId()+"/izvestajVolonter", izvestaj.getText().toString().trim());
                        map.put(baranje.getAktivnostId()+"/ocenaPostaroLice", Ocena);

                        firebaseDatabase.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Toast.makeText(mContext, "Successfully submitted review for activity!", Toast.LENGTH_SHORT).show();
                                    DodadiOcena(baranje.getUserId(), Ocena);
                                } else {
                                    Toast.makeText(mContext, "An error has occurred!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        dialog.dismiss();
                    }
                });

                alert.setNegativeButton(Html.fromHtml("<font color='#FFFFFF'>Dismiss</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        });

        viewHolder.txtInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);

                alert.setTitle("Activity review");

                LinearLayout layout = new LinearLayout(mContext);
                layout.setOrientation(LinearLayout.VERTICAL);

                final TextView textView = new TextView(mContext);
                textView.setText("Your review:");
                textView.setPadding(0,10,0,0);
                textView.setTextSize(20);

                final TextView textView1 = new TextView(mContext);
                textView1.setText(baranje.getIzvestajVolonter());
                textView1.setPadding(0,10,0,0);
                textView1.setTextSize(16);

                final TextView textView2 = new TextView(mContext);
                textView2.setText("Rating: " + String.valueOf(baranje.getOcenaPostaroLice()));
                textView2.setPadding(0,10,0,0);
                textView2.setTextSize(16);

                layout.addView(textView);
                layout.addView(textView1);
                layout.addView(textView2);

                if(baranje.getOcenaVolonter() != 0) {
                    final TextView textView3 = new TextView(mContext);
                    textView3.setText("User review:");
                    textView3.setPadding(0,10,0,0);
                    textView3.setTextSize(20);

                    final TextView textView4 = new TextView(mContext);
                    textView4.setText(baranje.getIzvestajPostaroLice());
                    textView4.setPadding(0,10,0,0);
                    textView4.setTextSize(16);

                    final TextView textView5 = new TextView(mContext);
                    textView5.setText("Rating: " + String.valueOf(baranje.getOcenaVolonter()));
                    textView5.setPadding(0,10,0,0);
                    textView5.setTextSize(16);

                    layout.addView(textView3);
                    layout.addView(textView4);
                    layout.addView(textView5);
                }

                alert.setView(layout);

                alert.setNegativeButton(Html.fromHtml("<font color='#FFFFFF'>Okay</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });

                alert.show();

            }
        });
        viewHolder.Pic.setImageResource(R.drawable.pozadina);
    }

    @Override
    public int getItemCount() {
        return myList == null ? 0 : myList.size();
    }

    private void DodadiOcena(String id, int ocena) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(id);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                int VkupnoOceni = user.getVkupnoOceni() + 1;
                int ZbirOceni = user.getZbirOceni() + ocena;
                user.setVkupnoOceni(VkupnoOceni);
                user.setZbirOceni(ZbirOceni);
                snapshot.getRef().setValue(user);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mContext, "An error has occurred!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

