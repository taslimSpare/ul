package app.taslimoseni.abcdef.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import app.taslimoseni.abcdef.R
import app.taslimoseni.abcdef.data.models.*
import app.taslimoseni.abcdef.databinding.FragmentMyLessonsBinding
import app.taslimoseni.abcdef.ui.LessonViewModel
import app.taslimoseni.abcdef.ui.adapters.LessonClickListener
import app.taslimoseni.abcdef.ui.adapters.MyLessonsAdapter
import app.taslimoseni.abcdef.utils.SimpleFunctions.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyLessonsFragment : Fragment(R.layout.fragment_my_lessons), LessonClickListener {

    private var _binding: FragmentMyLessonsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LessonViewModel by activityViewModels()
    private lateinit var adapter: MyLessonsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentMyLessonsBinding.bind(view)

        setupUI()
        setupObservers()
    }

    private fun setupUI() {
        adapter = MyLessonsAdapter(requireContext())
        binding.rvLessonList.adapter = adapter
        adapter.lessonClickListener = this

        val adapter = ArrayAdapter<CharSequence>(requireContext(), R.layout.spinner_text, resources.getStringArray(R.array.options))
        adapter.setDropDownViewResource(R.layout.spinner_drop_down_text)
        binding.spFilter.adapter = adapter

        binding.swipeRefreshLayout.isSoundEffectsEnabled = true
        binding.swipeRefreshLayout.setOnRefreshListener { viewModel.refresh() }
    }

    private fun setupObservers() {
        viewModel.getMyLessons().observe(viewLifecycleOwner, {
            when(it) {
                is Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }
                is Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    it.data?.data.let { list ->
                        if(list.isNullOrEmpty()) {
                            binding.rvLessonList.isGone = true
                            binding.cvEmptyState.isGone = false
                        } else {
                            binding.rvLessonList.isGone = false
                            binding.cvEmptyState.isGone = true
                            adapter.submitList(list)

                            binding.spFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    val newList = if(position != 0) list.filter { item -> item.subject.name.lowercase() == binding.spFilter.selectedItem.toString().lowercase() } else list
                                    if(newList.isEmpty()) {
                                        binding.rvLessonList.isGone = true
                                        binding.cvEmptyState.isGone = false
                                    }
                                    else {
                                        binding.rvLessonList.isGone = false
                                        binding.cvEmptyState.isGone = true
                                        adapter.submitList(newList)
                                    }
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) { }
                            }

                            binding.spFilter.setSelection(0)
                        }
                    }
                }
                is Failed, is Error -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    it.message?.let { message ->
                        requireActivity().showSnackBar(message)
                    }
                }
            }
        })
    }

    override fun onLessonClicked(lesson: Lesson) {
        if(lesson.status.lowercase() == getString(R.string.live).lowercase()) requireActivity().showSnackBar(lesson.topic)
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}