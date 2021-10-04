package com.example.poke;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE;

public class FridgeSwipe extends ItemTouchHelper.Callback{
    private FridgeSwipeController.ButtonsState buttonShowedState = FridgeSwipeController.ButtonsState.GONE;
    private RectF buttonInstance;
    private static final float buttonWidth = 300;
    private RecyclerView.ViewHolder currentItemViewHolder = null;
    private int currentPosition = -1;
    private int previousPosition = -1;
    private float currentDx = 0f;
    private float clamp = 0f;
    private FridgeSwipeControllerActions buttonsActions = null;
    Paint p;
    private boolean swipeBack = false;

    public FridgeSwipe(){
    }

    public FridgeSwipe(FridgeSwipeControllerActions buttonsActions) {
        this.buttonsActions = buttonsActions;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.LEFT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        currentDx = 0f;
        previousPosition = viewHolder.getAdapterPosition();

        if (viewHolder != null) {
            final View foregroundView = ((FridgeAdapter.ViewHolder) viewHolder).itemView;
            getDefaultUIUtil().clearView(foregroundView);
        }
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            final View foregroundView = ((FridgeAdapter.ViewHolder) viewHolder).itemView;
            currentPosition = viewHolder.getAdapterPosition();
            getDefaultUIUtil().onSelected(foregroundView);
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ACTION_STATE_SWIPE) {
            View view = this.getView(viewHolder);
            boolean isClamped = this.getTag(viewHolder);
            float x = this.clampViewPositionHorizontal(view, dX, isClamped, isCurrentlyActive);
            this.currentDx = x;
            ItemTouchHelper.Callback.getDefaultUIUtil().onDraw(c, recyclerView, view, x, dY, actionState, isCurrentlyActive);
        }
        else {
            setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
        currentItemViewHolder = viewHolder;
    }

    public void onDraw(Canvas c) {
        if (currentItemViewHolder != null) {
            drawButtons(c, currentItemViewHolder);
        }
    }

    private void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder) {
        float buttonWidthWithoutPadding = buttonWidth - 20;
        float corners = 16;

        View itemView = viewHolder.itemView;
        Paint p = new Paint();

        RectF rightButton = new RectF(itemView.getRight() - buttonWidthWithoutPadding, itemView.getTop(), itemView.getRight(), itemView.getBottom());
        p.setColor(Color.RED);
        c.drawRoundRect(rightButton, corners, corners, p);
        drawText("DELETE", c, rightButton, p);

        buttonInstance = null;
        if (buttonShowedState == FridgeSwipeController.ButtonsState.RIGHT_VISIBLE) {
            buttonInstance = rightButton;
        }
    }

    private void drawText(String text, Canvas c, RectF button, Paint p) {
        float textSize = 60;
        p.setColor(Color.WHITE);
        p.setAntiAlias(true);
        p.setTextSize(textSize);

        float textWidth = p.measureText(text);
        c.drawText(text, button.centerX() - (textWidth / 2), button.centerY() + (textSize / 2), p);
    }
    private void setTouchListener(Canvas c,
                                  RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder,
                                  float dX, float dY,
                                  int actionState, boolean isCurrentlyActive) {

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;

                if(swipeBack) {
                    if (dX < -buttonWidth)
                        buttonShowedState = FridgeSwipeController.ButtonsState.RIGHT_VISIBLE;

                    if (buttonShowedState != FridgeSwipeController.ButtonsState.GONE) {
                        setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        setItemsClickable(recyclerView, false);
                    }
                }
                return false;
            }
        });
    }

    private void setTouchDownListener(final Canvas c,
                                      final RecyclerView recyclerView,
                                      final RecyclerView.ViewHolder viewHolder,
                                      final float dX, final float dY,
                                      final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
                return false;
            }
        });
    }

    private void setTouchUpListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    onChildDraw(c, recyclerView, viewHolder, 0F, dY, actionState, isCurrentlyActive);
                    recyclerView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return false;
                        }
                    });
                    setItemsClickable(recyclerView, true);

                    if (buttonsActions != null && buttonInstance != null && buttonInstance.contains(event.getX(), event.getY())) {
                         if (buttonShowedState == FridgeSwipeController.ButtonsState.RIGHT_VISIBLE) {
                            buttonsActions.onRightClicked(viewHolder.getAdapterPosition());
                        }
                    }
                    buttonShowedState = FridgeSwipeController.ButtonsState.GONE;
                    currentItemViewHolder = null;
                }
                return false;
            }
        });
    }

    private void setItemsClickable(RecyclerView recyclerView,
                                   boolean isClickable) {
        for (int i = 0; i < recyclerView.getChildCount(); ++i) {
            recyclerView.getChildAt(i).setClickable(isClickable);
        }
    }


    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        return defaultValue * 10;
    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        boolean isClamped = this.getTag(viewHolder);
        this.setTag(viewHolder, !isClamped && this.currentDx <= -this.clamp);
        return 2.0F;
    }

    private final float clampViewPositionHorizontal(View view, float dX, boolean isClamped, boolean isCurrentlyActive) {
        return dX = Math.min(dX, -buttonWidth);
    }

    private final void setTag(RecyclerView.ViewHolder viewHolder, boolean isClamped) {
        View var10000 = viewHolder.itemView;
        var10000.setTag(isClamped);
    }

    private final boolean getTag(RecyclerView.ViewHolder viewHolder) {
        View var10000 = viewHolder.itemView;
        Object var2 = var10000.getTag();
        if (!(var2 instanceof Boolean)) {
            var2 = null;
        }

        return (Boolean)var2 != null ? (Boolean)var2 : false;
    }

    private View getView(RecyclerView.ViewHolder viewHolder) {
            return viewHolder.itemView;
    }

    public final void setClamp(float clamp) {
        this.clamp = clamp;
    }

    public void clearCanvas() {
        Paint p = new Paint();
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        Canvas c = new Canvas();
        c.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);
    }

    public final void removePreviousClamp(RecyclerView recyclerView) {
        if(currentPosition == previousPosition) return;

        if(previousPosition != -1){
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(previousPosition);
            this.getView(viewHolder).setTranslationX(0.0F);
            this.setTag(viewHolder, false);
            this.previousPosition = -1;
        }
        else return;
    }

}
