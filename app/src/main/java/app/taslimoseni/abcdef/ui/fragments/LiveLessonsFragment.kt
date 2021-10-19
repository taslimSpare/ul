package app.taslimoseni.abcdef.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isGone
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import app.taslimoseni.abcdef.R
import app.taslimoseni.abcdef.data.models.*
import app.taslimoseni.abcdef.databinding.FragmentLiveLessonsBinding
import app.taslimoseni.abcdef.ui.LessonViewModel
import app.taslimoseni.abcdef.ui.adapters.LessonClickListener
import app.taslimoseni.abcdef.ui.adapters.LessonsAdapter
import app.taslimoseni.abcdef.ui.adapters.LiveLessonViewPagerAdapter
import app.taslimoseni.abcdef.utils.SimpleFunctions.showSnackBar
import app.taslimoseni.abcdef.utils.SimpleFunctions.tryNavigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LiveLessonsFragment : Fragment(R.layout.fragment_live_lessons), LessonClickListener {

    private var _binding: FragmentLiveLessonsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LessonViewModel by activityViewModels()
    private lateinit var adapter: LessonsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLiveLessonsBinding.bind(view)

        setupUI()
        setupObservers()

    }


    private fun setupUI() {
        adapter = LessonsAdapter(requireContext())
        binding.rvLessonList.adapter = adapter
        adapter.lessonClickListener = this

        val adapter = ArrayAdapter<CharSequence>(requireContext(), R.layout.spinner_text, resources.getStringArray(R.array.options))
        adapter.setDropDownViewResource(R.layout.spinner_drop_down_text)
        binding.spFilter.adapter = adapter

        binding.swipeRefreshLayout.isSoundEffectsEnabled = true
        binding.swipeRefreshLayout.setOnRefreshListener { viewModel.refresh() }

        binding.fabLessons.setOnClickListener {
            findNavController().tryNavigate(R.id.action_liveLessonsFragment_to_myLessonsFragment)
        }
    }

    private fun setupObservers() {
        viewModel.getLessonsUnderLive().observe(viewLifecycleOwner, {
            when(it) {
                is Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }
                is Success -> {
                    binding.spFilter.isGone = false
                    binding.ctaProceed.isGone = false
                    binding.fabLessons.isGone = false
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
                    binding.spFilter.isGone = false
                    binding.ctaProceed.isGone = false
                    binding.fabLessons.isGone = false
                    it.message?.let { message ->
                        requireActivity().showSnackBar(message)
                    }
                }
            }
        })

        viewModel.getPromotedCards().observe(viewLifecycleOwner, {
            when(it) {
                is Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }
                is Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    it.data?.data.let { list ->
                        binding.viewpager.adapter = LiveLessonViewPagerAdapter(childFragmentManager, lifecycle, list!!)
                        binding.viewpager.setCurrentItem(0, false)
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