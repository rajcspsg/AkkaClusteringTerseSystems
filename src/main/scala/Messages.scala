case object BackendRegistration
case class TransformationJob(msg: String)
case class TransformationResult(msg: String)
case class JobFailed(msg: String, job: TransformationJob)
