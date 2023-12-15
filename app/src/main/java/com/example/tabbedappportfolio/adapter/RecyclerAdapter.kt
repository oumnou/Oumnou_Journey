package com.example.tabbedappportfolio.adapter
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.tabbedappportfolio.R
import com.example.tabbedappportfolio.dbhelper.ItemsDBHelper
import com.example.tabbedappportfolio.model.Item


class RecyclerAdapter(var itemList: List<Item>,var context: Context) : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>(),
    ItemTouchHelperAdapter {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_recycle, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemList[position]

        holder.textView1.text = item.title
        holder.textView2.text = item.description

        holder.shareButton.setOnClickListener {
            shareItem(item.title, item.description, item.imagePath, context)
        }

        if (item.imagePath != "") {
        val contentResolver = holder.itemView.context.contentResolver
        try {
            // Use MediaStore to get the content URI for the file path
            val contentUri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            val projection = arrayOf(MediaStore.Images.Media._ID)
            val selection = "${MediaStore.Images.Media.DATA} = ?"
            val selectionArgs = arrayOf(item.imagePath)
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
                cursor?.close()

            }
        } catch (e: Exception) {}

    }
        else{
                holder.imageView.visibility =  GONE

            }



    }
    private fun shareItem(title: String, text: String, photoUrl: String, context: Context) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"

        // You can modify the sharing message format as per your preference
        val shareMessage = "Hi! Check my new experience in Life : \n\n Title : $title\n Description : $text"

        // Add the photo URL to the message (if available)
        if (photoUrl.isNotEmpty()) {
            shareMessage.plus("\n\nPhoto: $photoUrl")
        }

        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        context.startActivity(Intent.createChooser(shareIntent, "Share via"))
    }


    override fun getItemCount(): Int {
        return itemList.size
    }

    // ViewHolder class
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView1: TextView = itemView.findViewById(R.id.tv_title)
        val textView2: TextView = itemView.findViewById(R.id.tv_text)
        val imageView: ImageView = itemView.findViewById(R.id.image)
        val shareButton: ImageView = itemView.findViewById(R.id.share_btn)

    }



    fun updateData(newItemList: List<Item>) {
        itemList = newItemList
        notifyDataSetChanged()


    }

    override fun onItemDismiss(position: Int) {
        itemList = itemList.toMutableList().apply { removeAt(position) }
        notifyItemRemoved(position)
    }



}
interface ItemTouchHelperAdapter {
    fun onItemDismiss(position: Int)
}


class ItemTouchHelperCallback(private val adapter: RecyclerAdapter, val context :Context, val categoryType: String) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

    private val background: ColorDrawable = ColorDrawable(Color.argb(67,67,4,107))
    private val deleteIcon: Drawable? =
        ContextCompat.getDrawable(context, R.drawable.delete_btn)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val deletedItem = adapter.itemList[position]

        val dbHelper = ItemsDBHelper(context, categoryType)
        dbHelper.deleteItem(deletedItem)

        adapter.onItemDismiss(position)
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
            itemView.left + dX.toInt() - backgroundCornerOffset,
            itemView.top,
            itemView.left,
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

