import { gql, useQuery } from '@apollo/client'
import './App.css'

// GraphQL query
const GET_JOBS = gql`
  query GetJobs {
    jobs {
      id
      title
      company {
        name
      }
    }
  }
`
const CREATE_JOB = gql`
  mutation CreateJob(
    $title: String!
    $description: String!
    $location: String!
    $companyId: Long!
  ) {
    createJob(
      title: $title
      description: $description
      location: $location
      companyId: $companyId
    ) {
      id
      title
      company {
        name
      }
    }
  }
`


// TypeScript types for data
interface Company {
  name: string
}
interface Job {
  id: string
  title: string
  company: Company
}

interface JobsData {
  jobs: Job[]
}

function App() {
  const { loading, error, data } = useQuery<JobsData>(GET_JOBS)

  return (
    <div className="container">
      <h1>Job Listings</h1>

      {loading && <p>Loading jobs...</p>}
      {error && <p>Error: {error.message}</p>}

      {data && (
        <div className="job-list">
          {data.jobs.map((job) => (
            <div className="job-item" key={job.id}>
              <strong>{job.title}</strong>
              <br />
              <span>at {job.company.name}</span>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}

export default App
