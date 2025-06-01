import { useQuery, useMutation } from '@apollo/client'
import React from 'react'
import shoppingCartIcon from './assets/shopping-cart.png'
import userIcon from './assets/will.jpg'
import trashIcon from './assets/bin.png'
import { GET_JOBS, DELETE_JOB } from './graphql'
import './App.css'



// TypeScript types for data
interface Company {
  name: string
}
interface Job {
  id: string
  title: string
  company: Company
  description: string
  salary: number
}

interface JobsData {
  jobs: Job[]
}

// Convert regular number into currency format
function formatCurrency(amount: number) {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
  }).format(amount)
}

function App() {
  const { loading, error, data: job_data } = useQuery<JobsData>(GET_JOBS)
  const [selectedJob, setSelectedJob] = React.useState<Job | null>(null)
  const [searchTerm, setSearchTerm] = React.useState('')
  const [filterSection, setFilterSection] = React.useState(false)
  const [maxSalary, setMaxSalary] = React.useState<number | null>(null)
  const [minSalary, setMinSalary] = React.useState<number | null>(null)
  // Mutation to delete a job
  const [deleteJob] = useMutation(DELETE_JOB, {
    refetchQueries: [{ query: GET_JOBS }], // Refetch the jobs after deletion
    onCompleted: () => {
      // Optionally, you can refetch the jobs or update the UI after deletion
      setSelectedJob(null)
    },
    onError: (error) => {
      console.error('Error deleting job:', error)
    },
  })


  const handleJobClick = (job: Job) => {
    setSelectedJob(job)
  }
  // Filter jobs based on the search input (by title)
  const filteredJobs = job_data?.jobs.filter((job) =>
    job.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
    job.company.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    job.description.toLowerCase().includes(searchTerm.toLowerCase()) 
  )

  return (
    <div className="container">
      <header className="header">
        <h1>Pathnero</h1>
        <div className="search-container">
          <input
            type="search"
            placeholder="Search for jobs..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="search-input"
          />
          <button onClick={() => setFilterSection(!filterSection)}>
            Filters
          </button>
        </div>
        <div className="header-icons">
          <img
            src={shoppingCartIcon}
            alt="Shopping Cart"
            className="shopping-cart"
          />
          <img
            src={userIcon}
            alt="Profile"
            className="profile-picture"
          />
        </div>
      </header>

      {filterSection && (
        <div className="dropdown-container">
          <div className="salary-input">
            <label htmlFor="minSalary">Min Salary:</label>
            <input
              id="minSalary"
              type="number"
              placeholder="$0"
              value={minSalary !== null ? minSalary : ''}
              onChange={(e) => setMinSalary(e.target.value ? Number(e.target.value) : null)}
            />
          </div>
          <div className="salary-input">
            <label htmlFor="maxSalary">Max Salary:</label>
            <input
              id="maxSalary"
              type="number"
              placeholder="Any"
              value={maxSalary !== null ? maxSalary : ''}
              onChange={(e) => setMaxSalary(e.target.value ? Number(e.target.value) : null)}
            />
          </div>
        </div>
      )}

      {loading && <p>Loading jobs...</p>}
      {error && <p>Error: {error.message}</p>}

      {job_data && (
        <div className="job-container">
          {/* Left Column: Job List */}
          <div className="job-list">
            {filteredJobs && filteredJobs.length > 0 ? (
              filteredJobs.map((job) => (
                <div
                  className="job-item"
                  key={job.id}
                  onClick={() => handleJobClick(job)}
                >
                  <strong>{job.title}</strong>
                  <br />
                  <span>at {job.company.name}</span>
                </div>
              ))
            ) : (
              <p>No jobs found.</p>
            )}
          </div>

          {/* Right Column: Job Details */}
          <div className="job-details">
            {selectedJob ? (
              <>
                <div className="job-header">
                  <h2>{selectedJob.title}</h2>
                  <img src={trashIcon} alt="Trash" className="delete-icon" onClick={() => deleteJob({ variables: { id: selectedJob.id } })} />
                </div>
                <p>
                  <strong>Company:</strong> {selectedJob.company.name}
                </p>
                <p>
                  <strong>Salary:</strong> {formatCurrency(selectedJob.salary)}
                </p>
                <p>
                  <strong>Description:</strong> {selectedJob.description}
                </p>
              </>
            ) : (
              <p>Select a job to see the details</p>
            )}
          </div>
        </div>
      )}
    </div>
  )
}

export default App
