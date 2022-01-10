package com.example.zhivototekrug;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class myAdapterPostaroLice extends RecyclerView.Adapter<myAdapterPostaroLice.ViewHolder> {

    private List<Baranje> myList;
    private int rowLayout;
    private Context mContext;

    private User volonter;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtAktivnost, txtOpisAktivnost, txtVreme, txtAdresa, txtStatus,
                txtDelete, txtVolonter, txtInfo;
        public ImageView Pic;

        public ViewHolder(View itemView) {
            super(itemView);
            txtAktivnost = (TextView) itemView.findViewById(R.id.rwActivity);
            txtOpisAktivnost = (TextView) itemView.findViewById(R.id.rwActivityDesc);
            txtVreme = (TextView) itemView.findViewById(R.id.rwTime);
            txtAdresa = (TextView) itemView.findViewById(R.id.rwAddress);
            txtStatus = (TextView) itemView.findViewById(R.id.rwStatus);
            txtDelete = (TextView) itemView.findViewById(R.id.rwDelete);
            txtVolonter = (TextView) itemView.findViewById(R.id.rwVolunteer);
            txtInfo = (TextView) itemView.findViewById(R.id.rwInfo);
            Pic = (ImageView) itemView.findViewById(R.id.picture);

            txtInfo.setVisibility(View.INVISIBLE);
        }
    }

    public myAdapterPostaroLice(List<Baranje> myList, int rowLayout, Context context) {
        this.myList = myList;
        this.rowLayout = rowLayout;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Baranje baranje = myList.get(i);
        viewHolder.txtAktivnost.setText(baranje.getAktivnost());
        viewHolder.txtOpisAktivnost.setText("Description: " + baranje.getOpisAktivnost());
        if(baranje.getDatum().equals("")) {
            viewHolder.txtVreme.setText("Time: " + baranje.getVremeOd() + " - " + baranje.getVremeDo() + "       Every " + baranje.getDen());
        } else {
            viewHolder.txtVreme.setText("Time: " + baranje.getVremeOd() + " - " + baranje.getVremeDo() + "       Date: " + baranje.getDatum());
        }
        viewHolder.txtAdresa.setText("Address: " + baranje.getAdresa());
        viewHolder.txtStatus.setText(baranje.getStatus());

        viewHolder.txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                builder.setTitle("Delete");
                builder.setMessage("Are you sure you want to delete this activity?");

                builder.setPositiveButton(Html.fromHtml("<font color='#FFFFFF'>Yes</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Activities");
                        databaseReference.child(baranje.getAktivnostId()).removeValue();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton(Html.fromHtml("<font color='#FFFFFF'>No</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        viewHolder.txtStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(baranje.getStatus().equals("Waiting")) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users")
                            .child(baranje.getVolonterUId());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            volonter = snapshot.getValue(User.class);
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                            builder.setTitle("Confirm");
                            DecimalFormat decimalFormat = new DecimalFormat("##.0");
                            if(volonter.getVkupnoOceni() != 0) {
                                double ocena = (double) volonter.getZbirOceni() / volonter.getVkupnoOceni();
                                builder.setMessage(volonter.getName() + "    Rating: " + decimalFormat.format(ocena) + "\n"
                                        + "Email: " +volonter.getEmail() + "\n" + "Phone number: " + volonter.getPhone());
                            } else {
                                builder.setMessage(volonter.getName() + "    Rating: / \n"
                                        + "Email: " +volonter.getEmail() + "\n" + "Phone number: " + volonter.getPhone());
                            }

                            builder.setPositiveButton(Html.fromHtml("<font color='#FFFFFF'>Accept</font>"), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    Map<String, Object> map = new HashMap();

                                    DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("Activities");
                                    map.put(baranje.getAktivnostId()+"/status", "Reserved");
                                    map.put(baranje.getAktivnostId()+"/emailVolonter", volonter.getEmail());
                                    map.put(baranje.getAktivnostId()+"/telefonVolonter", volonter.getPhone());

                                    firebaseDatabase.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                Toast.makeText(mContext, "A volunteer has successfully been assigned to your activity!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(mContext, "An error has occurred!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                    dialog.dismiss();
                                }
                            });

                            builder.setNegativeButton(Html.fromHtml("<font color='#FFFFFF'>Cancel</font>"), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Map<String, Object> map = new HashMap();

                                    DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("Activities");
                                    map.put(baranje.getAktivnostId()+"/status", "Active");
                                    map.put(baranje.getAktivnostId()+"/volonterUId", "");

                                    firebaseDatabase.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                Toast.makeText(mContext, "Volunteer rejected.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(mContext, "An error occurred!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                    dialog.dismiss();
                                }
                            });

                            AlertDialog alert = builder.create();
                            alert.show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(mContext, "An error occurred!", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else if(baranje.getStatus().equals("Finished") && baranje.getOcenaVolonter() == 0) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(mContext);

                    alert.setTitle("Finished activity");

                    LinearLayout layout = new LinearLayout(mContext);
                    layout.setOrientation(LinearLayout.VERTICAL);

                    final TextView textView = new TextView(mContext);
                    textView.setText("Volunteer rating:");
                    textView.setPadding(0,10,0,0);
                    textView.setTextSize(16);

                    final EditText izvestaj = new EditText(mContext);
                    izvestaj.setHint("Add a review");
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
                            map.put(baranje.getAktivnostId()+"/izvestajPostaroLice", izvestaj.getText().toString().trim());
                            map.put(baranje.getAktivnostId()+"/ocenaVolonter", Ocena);

                            firebaseDatabase.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(mContext, "Successfully submitted review for finished activity!", Toast.LENGTH_SHORT).show();
                                        DodadiOcena(baranje.getVolonterUId(), Ocena);
                                    } else {
                                        Toast.makeText(mContext, "An error occurred!", Toast.LENGTH_SHORT).show();
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
            }
        });

        if(!baranje.getEmailVolonter().equals("")) {
            viewHolder.txtVolonter.setText("Contact: " + baranje.getEmailVolonter() + "       " + baranje.getTelefonVolonter());
        } else {
            viewHolder.txtVolonter.setText("");
        }

        if(baranje.getOcenaVolonter() == 0) {
            viewHolder.txtInfo.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.txtInfo.setVisibility(View.VISIBLE);
        }

        if(baranje.getOcenaVolonter() == 0 && (baranje.getStatus().equals("Finished") || baranje.getStatus().equals("Waiting"))) {
            viewHolder.txtStatus.setTextColor(Color.rgb(139,137,137));
            viewHolder.txtStatus.setPaintFlags(viewHolder.txtStatus.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        } else {
            viewHolder.txtStatus.setTextColor(Color.rgb(0,0,0));
            viewHolder.txtStatus.setPaintFlags(0);
        }

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
                textView1.setText(baranje.getIzvestajPostaroLice());
                textView1.setPadding(0,10,0,0);
                textView1.setTextSize(16);

                final TextView textView2 = new TextView(mContext);
                textView2.setText("Rating: " + String.valueOf(baranje.getOcenaVolonter()));
                textView2.setPadding(0,10,0,0);
                textView2.setTextSize(16);

                layout.addView(textView);
                layout.addView(textView1);
                layout.addView(textView2);

                if(baranje.getOcenaVolonter() != 0) {
                    final TextView textView3 = new TextView(mContext);
                    textView3.setText("Volunteer review:");
                    textView3.setPadding(0,10,0,0);
                    textView3.setTextSize(20);

                    final TextView textView4 = new TextView(mContext);
                    textView4.setText(baranje.getIzvestajVolonter());
                    textView4.setPadding(0,10,0,0);
                    textView4.setTextSize(16);

                    final TextView textView5 = new TextView(mContext);
                    textView5.setText("Rating: " + String.valueOf(baranje.getOcenaPostaroLice()));
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
                Toast.makeText(mContext, "An error occurred!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

