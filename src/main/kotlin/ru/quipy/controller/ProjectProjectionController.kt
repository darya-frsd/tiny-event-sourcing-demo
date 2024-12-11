package ru.quipy.controller

import org.springframework.web.bind.annotation.*
import ru.quipy.projections.ProjectProjection
import java.util.*

@RestController
@RequestMapping("/project-projections")
class ProjectProjectionController(
        private val projectProjection: ProjectProjection
) {
    @GetMapping("/{projectId}")
    fun getProject(@PathVariable projectId: UUID): ProjectProjection.ProjectData? {
        return projectProjection.getProjectById(projectId)
    }
}
