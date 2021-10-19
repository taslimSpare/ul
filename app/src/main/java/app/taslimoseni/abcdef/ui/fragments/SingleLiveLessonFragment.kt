package app.taslimoseni.abcdef.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import app.taslimoseni.abcdef.R
import app.taslimoseni.abcdef.data.models.Lesson
import app.taslimoseni.abcdef.databinding.FragmentSingleLiveLessonBinding
import app.taslimoseni.abcdef.utils.SimpleFunctions.formatDateToHumanReadableTime
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SingleLiveLessonFragment : Fragment(R.layout.fragment_single_live_lesson) {

    private var _binding: FragmentSingleLiveLessonBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSingleLiveLessonBinding.bind(view)

        setupUI()

    }

    private fun setupUI() {
        requireArguments().let { bundle ->
            binding.tvStatus.text = bundle.getString("status")?.uppercase()
            binding.tvTopic.text = bundle.getString("topic")
            binding.tvTime.text = bundle.getString("time")?.formatDateToHumanReadableTime()
            binding.tvInstructor.text = bundle.getString("instructor")

            when(bundle.getString("status")) {
                getString(R.string.live) -> {
                    binding.tvStatus.background.setTint(ContextCompat.getColor(requireContext(), R.color.app_red))
                    binding.tvStatus.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(requireContext(), R.drawable.ic_live), null, null, null)
                }
                getString(R.string.upcoming) -> {
                    binding.tvStatus.background.setTint(ContextCompat.getColor(requireContext(), R.color.app_gray))
                    binding.tvStatus.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(requireContext(), R.drawable.ic_upcoming), null, null, null)
                }
                else -> {
                    binding.tvStatus.background.setTint(ContextCompat.getColor(requireContext(), R.color.app_orange))
                    binding.tvStatus.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(requireContext(), R.drawable.ic_replay), null, null, null)
                }
            }

            binding.tvStatus.compoundDrawablePadding = 10

            Glide.with(requireContext()).load(bundle.getString("image")).into(binding.ivImage)
        }
    }

    companion object {

        fun newInstance(lesson: Lesson) = SingleLiveLessonFragment().apply {
            arguments = Bundle().apply {
                putAll(bundleOf(
                    "status" to lesson.status,
                    "topic" to lesson.topic,
                    "instructor" to "${lesson.tutor.firstName} ${lesson.tutor.lastName}",
                    "time" to lesson.startAt,
                    "image" to lesson.imageUrl
                ))
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}