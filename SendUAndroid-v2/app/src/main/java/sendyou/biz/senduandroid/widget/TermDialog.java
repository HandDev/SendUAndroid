package sendyou.biz.senduandroid.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import sendyou.biz.senduandroid.R;

/**
 * Created by pyh42 on 2017-01-08.
 */

public class TermDialog extends Dialog {

    private static final String TAG = "TermDialog";
    private String text;
    private String title;

    @BindView(R.id.term_text) TextView term;
    @BindView(R.id.title) TextView title_text;

    public TermDialog(Context context, String text, String title) {
        super(context);
        this.text = text;
        this.title = title;
    }

    public TermDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected TermDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_term);
        ButterKnife.bind(this);

        title_text.setText(title);
        term.setText(text);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.dismiss();
    }
}
