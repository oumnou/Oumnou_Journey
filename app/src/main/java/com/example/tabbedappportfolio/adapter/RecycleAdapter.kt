package com.example.tabbedappportfolio.adapter

import android.content.ContentUris
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.tabbedappportfolio.R
import com.example.tabbedappportfolio.model.MyItem


class RecycleAdapter(private var itemList: List<MyItem>) : RecyclerView.Adapter<RecycleAdapter.MyViewHolder>(),
    ItemTouchHelperAdapter {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.my_recycle, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemList[position]

        holder.textView1.text = item.title
        holder.textView2.text = item.text

        val contentResolver = holder.itemView.context.contentResolver
        try {
            // Use MediaStore to get the content URI for the file path
            val contentUri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            val projection = arrayOf(MediaStore.Images.Media._ID)
            val selection = "${MediaStore.Images.Media.DATA} = ?"
            val selectionArgs = arrayOf(item.imageResource)
            val cursor = contentResolver.query(contentUri, projection, selection, selectionArgs, null)

            if (cursor != null && cursor.moveToFirst()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                val uri = ContentUris.withAppendedId(contentUri, id)

                // Open an InputStream for the content URI
                val inputStream = contentResolver.openInputStream(uri)

                // Decode the bitmap
                val bitmap = BitmapFactory.decodeStream(inputStream)

                // Set the bitmap to the ImageView
                holder.imageView.setImageBitmap(bitmap)
            }

            cursor?.close()
        } catch (e: Exception) {}


    }


    override fun getItemCount(): Int {
        return itemList.size
    }

    // ViewHolder class
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView1: TextView = itemView.findViewById(R.id.tv_title)
        val textView2: TextView = itemView.findViewById(R.id.tv_text)
        val imageView: ImageView = itemView.findViewById(R.id.image)
    }



    fun updateData(newItemList: List<MyItem>) {
        itemList = newItemList
        notifyDataSetChanged()


    }

    override fun onItemDismiss(position: Int) {
        val deletedItem = itemList[position]
        // Notify the adapter about the item removal
        notifyItemRemoved(position)

        // Handle database deletion logic or invoke the delete callback
        //deleteCallback?.invoke(deletedItem)
    }



}
interface ItemTouchHelperAdapter {
    fun onItemDismiss(position: Int)
}


class ItemTouchHelperCallback(private val adapter: RecycleAdapter,context :Context) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

    private val background: ColorDrawable = ColorDrawable(Color.RED)
    private val deleteIcon: Drawable? =
        ContextCompat.getDrawable(context, R.drawable.ic_delete)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        //adapter.deleteCallback.invoke(adapter.itemList[position])
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val itemView = viewHolder.itemView
        val backgroundCornerOffset = 20

        // Draw red background
        background.setBounds(
            itemView.right + dX.toInt() - backgroundCornerOffset,
            itemView.top,
            itemView.right,
            itemView.bottom
        )
        background.draw(c)

        // Draw delete icon
        val deleteIconTop = itemView.top + (itemView.height - deleteIcon!!.intrinsicHeight) / 2
        val deleteIconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2
        val deleteIconLeft = itemView.right - deleteIconMargin - deleteIcon.intrinsicWidth
        val deleteIconRight = itemView.right - deleteIconMargin
        val deleteIconBottom = deleteIconTop + deleteIcon.intrinsicHeight

        deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
        deleteIcon.draw(c)
    }
}

