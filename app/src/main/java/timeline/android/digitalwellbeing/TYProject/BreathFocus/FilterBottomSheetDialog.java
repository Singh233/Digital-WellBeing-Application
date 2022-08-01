package timeline.android.digitalwellbeing.TYProject.BreathFocus;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import timeline.android.digitalwellbeing.TYProject.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FilterBottomSheetDialog extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;
    private SeekBar sb1;
    private  SeekBar sb2;
    private  SeekBar sb3;

    TextView breathIn;
    TextView breathOut;
    TextView Hold;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.filter_popup,container,false);

        sb1 = v.findViewById(R.id.breathInSeekBar);
        sb2 = v.findViewById(R.id.breathOutSeekBar);
        sb3 = v.findViewById(R.id.holdSeekBar);

        breathIn = v.findViewById(R.id.breathIn_0);
        breathOut = v.findViewById(R.id.breathOut_0);
        Hold = v.findViewById(R.id.hold_0);

        Button reset = v.findViewById(R.id.reset_btn);

        sb1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                breathIn.setText(""+sb1.getProgress());
                mListener.onInhaleProgressChanged(sb1.getProgress()*1000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        sb2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                breathOut.setText(""+sb2.getProgress());
                mListener.onExhaleProgressChanged(sb2.getProgress()*1000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        sb3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Hold.setText(""+sb3.getProgress());
                mListener.onHoldProgressChanged(sb3.getProgress()*1000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListener.onButtonClicked(4000,2000,4000);
                dismiss();
            }
        });

        return v;
    }
    public interface BottomSheetListener{
        void onButtonClicked(int inhale, int exhale, int hold);

        void onInhaleProgressChanged(int inhale);
        void onExhaleProgressChanged(int exhale);
        void onHoldProgressChanged(int hold);

    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (BottomSheetListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;

    }
}
