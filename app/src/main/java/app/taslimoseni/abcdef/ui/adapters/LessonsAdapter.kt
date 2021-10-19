package app.taslimoseni.abcdef.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.taslimoseni.abcdef.data.models.Lesson
import com.bumptech.glide.Glide
import androidx.core.content.ContextCompat
import app.taslimoseni.abcdef.R
import app.taslimoseni.abcdef.databinding.LiveLessonListingItemBinding
import app.taslimoseni.abcdef.utils.SimpleFunctions.formatDateToHumanReadableTime


class LessonsAdapter(var context: Context) : ListAdapter<Lesson, LessonsAdapter.ViewHolder>(LessonListDiffCallback()) {

    var lessonClickListener: LessonClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, context, lessonClickListener)
    }


    class ViewHolder(private val binding: LiveLessonListingItemBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Lesson, context: Context, lessonClickListener: LessonClickListener?) {
            binding.apply {

                tvTopic.text = item.topic
                tvTime.text = item.startAt.formatDateToHumanReadableTime()
                tvStatus.text = item.status.uppercase()
                tvSubject.text = item.subject.name
                tvInstructor.text = "${item.tutor.firstName} ${item.tutor.lastName}"
                tvStatus.compoundDrawablePadding = 10

                when(item.status) {
                    context.getString(R.string.live) -> {
                        tvStatus.background.setTint(ContextCompat.getColor(context, R.color.app_red))
                        tvStatus.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.ic_live), null, null, null)
                        tvSubject.setTextColor(ContextCompat.getColor(context, R.color.app_red))
                    }
                    context.getString(R.string.upcoming) -> {
                        tvStatus.background.setTint(ContextCompat.getColor(context, R.color.app_gray))
                        tvStatus.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.ic_upcoming), null, null, null)
                        tvSubject.setTextColor(ContextCompat.getColor(context, R.color.app_gray))
                    }
                    else -> {
                        tvStatus.background.setTint(ContextCompat.getColor(context, R.color.app_orange))
                        tvStatus.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.ic_replay), null, null, null)
                        tvSubject.setTextColor(ContextCompat.getColor(context, R.color.app_orange))
                    }
                }

                Glide.with(root.context).load(item.imageUrl).into(ivImage)
                root.setOnClickListener { lessonClickListener?.onLessonClicked(item) }
            }
        }

        companion object {

            fun from(parent: ViewGroup): ViewHolder {
                val binding = LiveLessonListingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ViewHolder(binding)
            }
        }
    }

}

class LessonListDiffCallback : DiffUtil.ItemCallback<Lesson>() {
    override fun areItemsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
        return oldItem == newItem
    }

}
