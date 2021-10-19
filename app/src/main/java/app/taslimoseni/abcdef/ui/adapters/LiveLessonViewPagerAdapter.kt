package app.taslimoseni.abcdef.ui.adapters


import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import app.taslimoseni.abcdef.data.models.Lesson
import app.taslimoseni.abcdef.ui.fragments.SingleLiveLessonFragment

class LiveLessonViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle, var lesson: List<Lesson>): FragmentStateAdapter(fm, lifecycle) {


    override fun createFragment(position: Int) = SingleLiveLessonFragment.newInstance(lesson[position])

    override fun getItemCount(): Int {
        return lesson.size
    }

}