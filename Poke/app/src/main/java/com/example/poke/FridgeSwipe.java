package com.example.poke;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
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
    private static final float buttonWidth = 300;
    private RectF buttonInstance;
    private RecyclerView.ViewHolder currentItemViewHolder = null;
    private int currentPosition = -1;
    private int previousPosition = -1;
    private float currentDx = 0f;
    private float clamp = 0f;

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
            final View foregroundView = ((IngredientAdapter.ViewHolder) viewHolder).itemView;
            getDefaultUIUtil().clearView(foregroundView);
        }
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {

        if (viewHolder != null) {
            final View foregroundView = ((IngredientAdapter.ViewHolder) viewHolder).itemView;
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
        currentItemViewHolder = viewHolder;
    }

    public void onDraw(Canvas c) {
        if (currentItemViewHolder != null) {
            drawButtons(c, currentItemViewHolder);
        }
    }

    //    private View getView (RecyclerView.ViewHolder viewHolder){
//        return (IngredientAdapter.SwipeViewHolder viewHolder).itemView.swipe_view
//    }

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
        float min = -((float)view.getWidth()) / (float)2;
        float max = 0.0F;
        float x = isClamped ? (isCurrentlyActive ? dX - this.clamp : -this.clamp) : dX;
        float var10 = Math.max(min, x);
        return Math.min(var10, max);
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

    private final View getView(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder == null) {
            throw new TypeCastException("null cannot be cast to non-null type com.example.poke.IngredientAdapter.ViewHolder");
        } else {
            View var10000 = ((com.example.poke.IngredientAdapter.ViewHolder)viewHolder).itemView;
            return var10000;
        }
    }

    public final void setClamp(float clamp) {
        this.clamp = clamp;
    }

    public final void removePreviousClamp(@NotNull RecyclerView recyclerView) {
        if (!Intrinsics.areEqual(this.currentPosition, this.previousPosition)) {
            Integer var10000 = this.previousPosition;
            if (var10000 != null) {
                Integer var2 = var10000;
                int it = ((Number)var2).intValue();
                RecyclerView.ViewHolder var8 = recyclerView.findViewHolderForAdapterPosition(it);
                if (var8 == null) {
                    return;
                }

                RecyclerView.ViewHolder viewHolder = var8;
                this.getView(viewHolder).setTranslationX(0.0F);
                this.setTag(viewHolder, false);
                this.previousPosition = (Integer)null;
            }

        }
    }

}
